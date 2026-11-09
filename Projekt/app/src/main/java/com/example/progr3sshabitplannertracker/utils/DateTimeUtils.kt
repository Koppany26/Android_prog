package com.example.progr3sshabitplannertracker.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateTimeUtils {
    fun todayDate(): String {
        return LocalDate.now().toString() // YYYY-MM-DD
    }

    fun formatTime(isoString: String?): String {
        if (isoString.isNullOrBlank()) return ""
        return try {
            val cleanStr = isoString.replace("Z", "")
            val ldt = LocalDateTime.parse(cleanStr)
            ldt.format(DateTimeFormatter.ofPattern("HH:mm", Locale.US))
        } catch (_: Exception) {
            ""
        }
    }

    fun toIsoDateTime(dateStr: String, timeStr: String): String {
        val cleanTime = timeStr.trim().ifEmpty { "12:00" }
        val parts = cleanTime.split(":")
        val hour = parts.getOrNull(0)?.toIntOrNull()?.coerceIn(0, 23) ?: 12
        val min = parts.getOrNull(1)?.toIntOrNull()?.coerceIn(0, 59) ?: 0
        val date = try { LocalDate.parse(dateStr) } catch (_: Exception) { LocalDate.now() }
        val time = LocalTime.of(hour, min)
        return LocalDateTime.of(date, time).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z"
    }
}
