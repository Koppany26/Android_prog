package com.example.progr3sshabitplannertracker.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.progr3sshabitplannertracker.data.model.HabitResponse
import com.example.progr3sshabitplannertracker.data.model.ScheduleResponse
import com.example.progr3sshabitplannertracker.data.repository.HabitRepository
import com.example.progr3sshabitplannertracker.data.repository.ScheduleRepository
import com.example.progr3sshabitplannertracker.utils.DateTimeUtils
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val today: String = DateTimeUtils.todayDate(),
    val isLoading: Boolean = true,
    val schedules: List<ScheduleResponse> = emptyList(),
    val habits: List<HabitResponse> = emptyList(),
    val error: String? = null
)

class HomeViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val scheduleRepo = ScheduleRepository(context)
            val habitRepo = HabitRepository(context)
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(scheduleRepo, habitRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

class HomeViewModel(
    private val scheduleRepository: ScheduleRepository,
    private val habitRepository: HabitRepository = HabitRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _schedules = MutableLiveData<List<ScheduleResponse>>()
    val schedules: LiveData<List<ScheduleResponse>> get() = _schedules

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun loadToday() {
        val today = DateTimeUtils.todayDate()
        _uiState.update { it.copy(today = today, isLoading = true, error = null) }
        viewModelScope.launch {
            val habitsDeferred = async { habitRepository.getHabits() }
            val schedulesDeferred = async { scheduleRepository.getScheduleByDay(today) }

            val habitsResult = habitsDeferred.await()
            val schedulesResult = schedulesDeferred.await()

            val error = schedulesResult.exceptionOrNull()?.message
                ?: habitsResult.exceptionOrNull()?.message

            val rawSchedules = schedulesResult.getOrDefault(emptyList())
            val loadedHabits = habitsResult.getOrDefault(emptyList())

            // Sort: Uncompleted items first, Completed items last. Then by startTime.
            val sortedSchedules = rawSchedules.sortedWith(
                compareBy<ScheduleResponse> { schedule ->
                    val isCompleted = schedule.status == "Completed" || schedule.progress?.any { it.progressValue > 0 } == true
                    if (isCompleted) 1 else 0
                }.thenBy { schedule ->
                    schedule.startTime ?: java.time.LocalDateTime.MAX
                }
            )

            _schedules.value = sortedSchedules
            _isLoading.value = false
            if (error != null) _errorMessage.value = error

            _uiState.update {
                it.copy(
                    isLoading = false,
                    habits = loadedHabits,
                    schedules = sortedSchedules,
                    error = error
                )
            }
        }
    }

    fun toggleProgress(schedule: ScheduleResponse) {
        viewModelScope.launch {
            val today = DateTimeUtils.todayDate()
            val isCompleted = schedule.status != "Completed"
            val result = scheduleRepository.createProgress(
                scheduleId = schedule.id,
                date = today,
                isCompleted = isCompleted
            )
            result.onSuccess {
                loadToday()
            }.onFailure { error ->
                _errorMessage.value = error.message
            }
        }
    }

    fun updateSchedule(id: Long, status: String?, notes: String?) {
        viewModelScope.launch {
            val result = scheduleRepository.updateSchedule(id, status = status, notes = notes)
            result.onSuccess {
                loadToday()
            }.onFailure { error ->
                _errorMessage.value = error.message
            }
        }
    }

    fun deleteSchedule(id: Long) {
        viewModelScope.launch {
            val result = scheduleRepository.deleteSchedule(id)
            result.onSuccess {
                loadToday()
            }.onFailure { error ->
                _errorMessage.value = error.message
            }
        }
    }

    fun getScheduleByDay(day: String) {
        loadToday()
    }

    fun clearError() {
        _errorMessage.value = null
        _uiState.update { it.copy(error = null) }
    }
}
