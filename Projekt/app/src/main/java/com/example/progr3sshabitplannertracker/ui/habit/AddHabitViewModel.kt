package com.example.progr3sshabitplannertracker.ui.habit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.progr3sshabitplannertracker.data.model.HabitCategory
import com.example.progr3sshabitplannertracker.data.repository.HabitRepository
import kotlinx.coroutines.launch

class AddHabitViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = HabitRepository(application)

    private val _categories = MutableLiveData<List<HabitCategory>>()
    val categories: LiveData<List<HabitCategory>> = _categories

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun fetchCategories() {
        viewModelScope.launch {
            try {
                val cats = repository.getCategories().getOrThrow()
                _categories.value = cats
            } catch (_: Exception) {
                // Fallback default 7 categories if network call fails
                val defaults = listOf(
                    HabitCategory(1, "Exercise", "/icons/gym.svg"),
                    HabitCategory(2, "Reading", "/icons/read.svg"),
                    HabitCategory(3, "Study", "/icons/code.svg"),
                    HabitCategory(4, "Hydration", "/icons/water.svg"),
                    HabitCategory(5, "Writing", "/icons/write.svg"),
                    HabitCategory(6, "Running", "/icons/run.svg"),
                    HabitCategory(7, "Other", "/icons/other.svg")
                )
                _categories.value = defaults
            }
        }
    }

    fun addHabit(name: String, description: String, categoryId: Int, goal: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                repository.createHabit(name, description, categoryId, goal).getOrThrow()
                _isSuccess.value = true
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to create habit"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
