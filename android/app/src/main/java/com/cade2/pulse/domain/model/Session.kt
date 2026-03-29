package com.cade2.pulse.domain.model

data class Session(
    val sessionId: String,
    val date: String,
    val status: String,
    val acceptedEmotions: List<EmotionCard>,
    val contextSocial: String?,
    val contextEnergy: String?,
    val contextSleep: String?
) {
    val isCompleted: Boolean get() = status.equals("completed", ignoreCase = true)
}

data class Streak(
    val currentStreak: Int,
    val longestStreak: Int
)
