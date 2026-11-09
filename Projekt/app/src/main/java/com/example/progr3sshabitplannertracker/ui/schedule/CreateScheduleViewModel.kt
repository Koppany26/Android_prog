package com.example.progr3sshabitplannertracker.ui.schedule

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.progr3sshabitplannertracker.data.model.ScheduleCreateRequest
import com.example.progr3sshabitplannertracker.data.repository.ScheduleRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Locale

class CreateScheduleViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateScheduleViewModel::class.java)) {
            val repository = ScheduleRepository(context)
            @Suppress("UNCHECKED_CAST")
            return CreateScheduleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class CreateScheduleViewModel(private val repository: ScheduleRepository) : ViewModel() {

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun createSchedule(habitName: String, timeStr: String, durationStr: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val habit = repository.getOrCreateHabit(habitName)

                val cleanTime = timeStr.trim().ifEmpty { "12:00" }
                val parts = cleanTime.split(":")
                val hour = parts.getOrNull(0)?.toIntOrNull()?.coerceIn(0, 23) ?: 12
                val min = parts.getOrNull(1)?.toIntOrNull()?.coerceIn(0, 59) ?: 0
                val dateStr = LocalDate.now().toString()
                val timeFormatted = String.format(Locale.US, "%02d:%02d:00", hour, min)
                val fullDateTime = "${dateStr}T${timeFormatted}Z"
                val duration = durationStr.toIntOrNull() ?: 30

                val request = ScheduleCreateRequest(
                    habitId = habit.id,
                    start_time = fullDateTime,
                    end_time = fullDateTime,
                    duration_minutes = duration,
                    repeatPattern = "daily",
                    repeatDays = 30,
                    is_custom = true,
                    participantIds = emptyList(),
                    notes = "Created from app"
                )

                repository.createSchedule(request)
                _isSuccess.value = true
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to save schedule"
            } finally {
                _isLoading.value = false
            }
        }
    }
}