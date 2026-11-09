package com.example.progr3sshabitplannertracker.data.network

import com.google.gson.Gson
import retrofit2.Response

data class ApiError(
    val message: String? = null
)

internal fun <T> Response<T>.bodyOrThrow(fallbackMessage: String): T {
    if (isSuccessful) {
        return body() ?: error("Empty response from server")
    }
    error(parseErrorMessage(this, fallbackMessage))
}

private fun parseErrorMessage(response: Response<*>, fallbackMessage: String): String {
    val raw = response.errorBody()?.string()
    if (!raw.isNullOrBlank()) {
        val parsed = try {
            Gson().fromJson(raw, ApiError::class.java)
        } catch (_: Exception) {
            null
        }
        if (!parsed?.message.isNullOrBlank()) {
            return parsed!!.message!!
        }
    }
    val message = response.message()
    return if (message.isNullOrBlank()) "$fallbackMessage (${response.code()})" else message
}
