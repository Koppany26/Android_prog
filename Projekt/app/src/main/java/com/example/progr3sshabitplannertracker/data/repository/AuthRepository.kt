package com.example.progr3sshabitplannertracker.data.repository

import android.content.Context
import com.example.progr3sshabitplannertracker.data.model.AuthResponseDto
import com.example.progr3sshabitplannertracker.data.model.SignInDto
import com.example.progr3sshabitplannertracker.data.model.SignUpDto
import com.example.progr3sshabitplannertracker.data.network.RetrofitClient
import retrofit2.Response

class AuthRepository(context: Context) {
    private val api = RetrofitClient.getInstance(context)

    suspend fun login(email: String, password: String): Response<AuthResponseDto> {
        return api.login(SignInDto(email = email, password = password))
    }

    suspend fun register(username: String, email: String, password: String): Response<AuthResponseDto> {
        return api.signup(SignUpDto(username = username, email = email, password = password))
    }
}
