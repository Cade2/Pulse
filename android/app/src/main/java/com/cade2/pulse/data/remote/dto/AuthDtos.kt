package com.cade2.pulse.data.remote.dto

data class RegisterRequestDto(
    val email: String,
    val password: String,
    val name: String
)

data class LoginRequestDto(
    val email: String,
    val password: String
)

data class AuthResponseDto(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
    val name: String,
    val email: String
)

data class RefreshRequestDto(
    val refreshToken: String
)

data class RefreshResponseDto(
    val accessToken: String,
    val refreshToken: String
)

data class FcmTokenRequestDto(
    val token: String
)
