package com.example.progr3sshabitplannertracker.utils

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeAdapter : JsonDeserializer<LocalDateTime>, JsonSerializer<LocalDateTime> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDateTime {
        val dateString = json.asString
        return try {
            ZonedDateTime.parse(dateString).toLocalDateTime()
        } catch (_: Exception) {
            try {
                LocalDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME)
            } catch (_: Exception) {
                try {
                    LocalDateTime.parse(dateString.replace("Z", ""))
                } catch (_: Exception) {
                    LocalDateTime.now()
                }
            }
        }
    }

    override fun serialize(
        src: LocalDateTime?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src?.format(DateTimeFormatter.ISO_DATE_TIME))
    }
}
