package com.cade2.pulse.data.remote.dto

data class EmotionCardDto(
    val cardId: String,
    val name: String,
    val description: String,
    val emoji: String,
    val category: String
)

data class TodaySessionDto(
    val sessionId: String?,
    val date: String,
    val status: String,
    val cards: List<EmotionCardDto>,
    val acceptedCardIds: List<String>?,
    val contextSocial: String?,
    val contextEnergy: String?,
    val contextSleep: String?
)

data class CompleteSessionRequestDto(
    val sessionId: String,
    val acceptedCardIds: List<String>,
    val contextSocial: String?,
    val contextEnergy: String?,
    val contextSleep: String?
)

data class CompleteSessionResponseDto(
    val sessionId: String,
    val status: String,
    val streakCount: Int
)

data class SessionHistoryItemDto(
    val sessionId: String,
    val date: String,
    val status: String,
    val acceptedEmotions: List<EmotionCardDto>,
    val dominantSentiment: String?
)

data class StreakResponseDto(
    val currentStreak: Int,
    val longestStreak: Int
)
