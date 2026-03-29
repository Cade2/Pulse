package com.cade2.pulse.data.remote.dto

data class WeeklyInsightDto(
    val weekStart: String,
    val weekEnd: String,
    val totalSessions: Int,
    val mostFrequentEmotion: EmotionCardDto?,
    val averageMood: String?,
    val topEmotions: List<EmotionFrequencyDto>
)

data class EmotionFrequencyDto(
    val card: EmotionCardDto,
    val count: Int,
    val percentage: Float
)

data class PatternDto(
    val patternId: String,
    val emoji: String,
    val title: String,
    val description: String,
    val detectedAt: String
)

data class CalendarDayDto(
    val date: String,
    val status: String,
    val dominantSentiment: String?
)

data class CalendarResponseDto(
    val month: Int,
    val year: Int,
    val days: List<CalendarDayDto>
)
