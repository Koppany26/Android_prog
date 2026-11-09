package com.example.progr3sshabitplannertracker.data.repository

import android.content.Context
import com.example.progr3sshabitplannertracker.data.model.CreateHabitDto
import com.example.progr3sshabitplannertracker.data.model.CreateProgressDto
import com.example.progr3sshabitplannertracker.data.model.HabitResponse
import com.example.progr3sshabitplannertracker.data.model.ProgressResponseDto
import com.example.progr3sshabitplannertracker.data.model.ScheduleCreateRequest
import com.example.progr3sshabitplannertracker.data.model.ScheduleResponse
import com.example.progr3sshabitplannertracker.data.model.UpdateScheduleDto
import com.example.progr3sshabitplannertracker.data.network.ApiService
import com.example.progr3sshabitplannertracker.data.network.RetrofitClient
import com.example.progr3sshabitplannertracker.data.network.bodyOrThrow

class ScheduleRepository(
    context: Context? = null,
    private val api: ApiService = RetrofitClient.getInstance(context)
) {
    suspend fun getScheduleByDay(day: String): Result<List<ScheduleResponse>> = runCatching {
        api.getScheduleByDay(day).bodyOrThrow("Failed to load schedules")
    }

    suspend fun getScheduleById(id: Long): Result<ScheduleResponse> = runCatching {
        api.getScheduleById(id).bodyOrThrow("Failed to load schedule details")
    }

    suspend fun getOrCreateHabit(habitName: String): HabitResponse {
        val habitsResult = runCatching { api.getHabits().bodyOrThrow("Failed") }
        val habits = habitsResult.getOrDefault(emptyList())

        // Find existing habit with matching name (case-insensitive)
        val existing = habits.firstOrNull { it.name.equals(targetName(habitName), ignoreCase = true) }
        if (existing != null) {
            return existing
        }

        // Create new habit if no match was found
        return api.createHabit(CreateHabitDto(name = targetName(habitName))).bodyOrThrow("Failed to create habit")
    }

    private fun targetName(habitName: String): String {
        return if (habitName.isBlank()) "Default Habit" else habitName.trim()
    }

    suspend fun createRecurringSchedule(request: ScheduleCreateRequest): Result<List<ScheduleResponse>> = runCatching {
        api.createRecurringSchedule(request).bodyOrThrow("Failed to create schedule")
    }

    suspend fun createCustomSchedule(request: ScheduleCreateRequest): Result<ScheduleResponse> = runCatching {
        api.createCustomSchedule(request).bodyOrThrow("Failed to create schedule")
    }

    suspend fun createSchedule(request: ScheduleCreateRequest): List<ScheduleResponse> {
        return createRecurringSchedule(request).getOrThrow()
    }

    suspend fun updateSchedule(id: Long, status: String? = null, notes: String? = null): Result<ScheduleResponse> = runCatching {
        api.updateSchedule(id, UpdateScheduleDto(status = status, notes = notes)).bodyOrThrow("Failed to update schedule")
    }

    suspend fun deleteSchedule(id: Long): Result<Unit> = runCatching {
        api.deleteSchedule(id)
        Unit
    }

    suspend fun createProgress(scheduleId: Long, date: String, isCompleted: Boolean = true): Result<ProgressResponseDto> = runCatching {
        api.createProgress(CreateProgressDto(scheduleId = scheduleId, date = date, is_completed = isCompleted)).bodyOrThrow("Failed to log progress")
    }
}
