package com.example.progr3sshabitplannertracker.data.repository

import android.content.Context
import com.example.progr3sshabitplannertracker.data.model.ProfileResponseDto
import com.example.progr3sshabitplannertracker.data.model.UpdateProfileDto
import com.example.progr3sshabitplannertracker.data.network.ApiService
import com.example.progr3sshabitplannertracker.data.network.RetrofitClient
import com.example.progr3sshabitplannertracker.data.network.bodyOrThrow
import okhttp3.MultipartBody

class UserRepository(
    context: Context? = null,
    private val api: ApiService = RetrofitClient.getInstance(context)
) {
    suspend fun getProfile(): Result<ProfileResponseDto> = runCatching {
        api.getProfile().bodyOrThrow("Failed to load profile")
    }

    suspend fun updateProfile(username: String, description: String? = null): Result<ProfileResponseDto> = runCatching {
        api.updateProfile(UpdateProfileDto(username = username, description = description)).bodyOrThrow("Failed to update profile")
    }

    suspend fun uploadProfileImage(imagePart: MultipartBody.Part): Result<ProfileResponseDto> = runCatching {
        api.uploadProfileImage(imagePart).bodyOrThrow("Failed to upload profile image")
    }

    suspend fun logout(): Result<Unit> = runCatching {
        api.logout()
        Unit
    }
}
