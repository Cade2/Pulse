package com.cade2.pulse.domain.model

data class User(
    val userId: String,
    val name: String,
    val email: String,
    val avatarColor: String,
    val notificationHour: Int,
    val notificationMinute: Int,
    val memberSince: String
)
