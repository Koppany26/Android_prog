package com.example.progr3sshabitplannertracker.data.repository

import android.content.Context
import com.example.progr3sshabitplannertracker.data.model.CreateHabitDto
import com.example.progr3sshabitplannertracker.data.model.HabitCategory
import com.example.progr3sshabitplannertracker.data.model.HabitResponse
import com.example.progr3sshabitplannertracker.data.network.ApiService
import com.example.progr3sshabitplannertracker.data.network.RetrofitClient
import com.example.progr3sshabitplannertracker.data.network.bodyOrThrow

class HabitRepository(
    context: Context? = null,
    private val api: ApiService = RetrofitClient.getInstance(context ?: RetrofitClient.appContext!!)
) {
    suspend fun getCategories(): Result<List<HabitCategory>> = runCatching {
        api.getHabitCategories().bodyOrThrow("Failed to load habit categories")
    }

    suspend fun getHabits(): Result<List<HabitResponse>> = runCatching {
        api.getHabits().bodyOrThrow("Failed to load habits")
    }

    suspend fun createHabit(
        name: String,
        description: String,
        categoryId: Int,
        goal: String
    ): Result<HabitResponse> = runCatching {
        api.createHabit(
            CreateHabitDto(
                name = name,
                description = description.ifBlank { null },
                categoryId = categoryId,
                goal = goal
            )
        ).bodyOrThrow("Failed to create habit")
    }
}
