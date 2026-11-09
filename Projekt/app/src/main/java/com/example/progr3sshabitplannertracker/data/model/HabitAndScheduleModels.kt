package com.example.progr3sshabitplannertracker.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class CreateHabitDto(
    val name: String,
    val categoryId: Int = 1,
    val goal: String = "Daily habit",
    val description: String? = "Created from app"
)

data class HabitCategory(
    val id: Long,
    val name: String,
    val iconUrl: String? = null
)

data class HabitResponse(
    val id: Long,
    val name: String,
    val description: String? = null,
    val category: HabitCategory? = null,
    val goal: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null
)

data class ScheduleResponse(
    val id: Long,
    @SerializedName("start_time")
    val startTime: LocalDateTime? = null,
    @SerializedName("end_time")
    val endTime: LocalDateTime? = null,
    val status: String? = null,
    val date: String? = null,
    @SerializedName("is_custom")
    val isCustom: Boolean,
    @SerializedName("created_at")
    val createdAt: LocalDateTime? = null,
    @SerializedName("updated_at")
    val updatedAt: LocalDateTime? = null,
    val type: String? = null,
    @SerializedName("duration_minutes")
    val durationMinutes: Int? = null,
    val notes: String? = null,
    val participants: List<ParticipantDto>? = emptyList(),
    val habit: HabitResponse? = null,
    val progress: List<ProgressResponseDto>? = emptyList(),
    val isParticipantOnly: Boolean
)

data class CreateProgressDto(
    val scheduleId: Long,
    val date: String,
    val logged_time: Double? = null,
    val notes: String? = null,
    val is_completed: Boolean = true
)

data class ProgressResponseDto(
    val id: Long,
    val userId: Long,
    val progressValue: Double,
    val updatedAt: LocalDateTime
)

data class ParticipantDto(
    val id: Long,
    val name: String,
    val email: String,
    val profileImage: String? = null
)

data class UpdateScheduleDto(
    val start_time: String? = null,
    val end_time: String? = null,
    val duration_minutes: Int? = null,
    val status: String? = null,
    val notes: String? = null
)

data class ProfileResponseDto(
    val id: Long,
    val email: String,
    val username: String? = null,
    val description: String? = null,
    val profileImageUrl: String? = null,
    @SerializedName("profileImageBase64")
    val profileImageBase64: String? = null
)

data class UpdateProfileDto(
    val username: String? = null,
    val description: String? = null
)
