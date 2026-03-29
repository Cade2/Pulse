package com.cade2.pulse.domain.model

data class WeeklyInsight(
    val weekStart: String,
    val weekEnd: String,
    val totalSessions: Int,
    val topEmotions: List<EmotionFrequency>,
    val mostFrequentEmotion: EmotionCard?
)

data class EmotionFrequency(
    val card: EmotionCard,
    val count: Int,
    val percentage: Float
)

data class Pattern(
    val patternId: String,
    val emoji: String,
    val title: String,
    val description: String
)

data class CalendarDay(
    val date: String,
    val status: String,
    val dominantSentiment: String?
)
