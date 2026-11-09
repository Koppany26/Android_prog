package com.example.progr3sshabitplannertracker.data.model

data class Habit(
    val id: Int,
    val name: String
)

enum class ScheduleStatus {
    PLANNED, COMPLETED, SKIPPED
}

enum class RepeatPattern(val displayName: String) {
    NONE("None"),
    DAILY("Daily"),
    WEEKDAYS("Weekdays"),
    WEEKENDS("Weekends")
}

data class Schedule(
    val id: Int,
    val habit: Habit,
    val startTime: String, // HH:mm string for simplicity in UI
    val status: ScheduleStatus,
    val durationMinutes: Int? = null,
    val repeatPattern: RepeatPattern = RepeatPattern.NONE
)
