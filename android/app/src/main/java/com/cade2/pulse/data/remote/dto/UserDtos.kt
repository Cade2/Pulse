package com.cade2.pulse.data.remote.dto

data class UserProfileDto(
    val userId: String,
    val name: String,
    val email: String,
    val avatarColor: String?,
    val notificationHour: Int,
    val notificationMinute: Int,
    val memberSince: String
)

data class UpdateSettingsRequestDto(
    val avatarColor: String?,
    val notificationHour: Int,
    val notificationMinute: Int
)

data class UpdateSettingsResponseDto(
    val success: Boolean,
    val message: String
)
