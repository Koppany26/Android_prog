package com.example.progr3sshabitplannertracker.data.model

// Kérések (Request)
data class SignInDto(
    val email: String,
    val password: String
)

data class SignUpDto(
    val username: String,
    val email: String,
    val password: String
    // Opcionálisan ide jöhet majd a profileImage, de a specifikáció szerint az Multipart, így azt máshogy küldjük (ha van)
)

data class ResetPasswordDto(
    val email: String
)

// Válaszok (Response)
data class AuthResponseDto(
    val message: String,
    val user: UserDto?,
    val tokens: TokensDto?
)

data class UserDto(
    val id: Int,
    val username: String,
    val email: String,
    val profileImageUrl: String? = null
)

data class TokensDto(
    val accessToken: String,
    val refreshToken: String
)
