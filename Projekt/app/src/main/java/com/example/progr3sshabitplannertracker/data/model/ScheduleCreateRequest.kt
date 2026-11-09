package com.example.progr3sshabitplannertracker.data.model

data class ScheduleCreateRequest(
    val habitId: Long,
    val start_time: String,
    val end_time: String,
    val duration_minutes: Int,
    val repeatPattern: String,
    val repeatDays: Int = 90,
    val is_custom: Boolean = true,
    val participantIds: List<Long> = emptyList(),
    val notes: String = ""
)
