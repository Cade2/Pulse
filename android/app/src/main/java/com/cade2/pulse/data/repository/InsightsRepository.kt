package com.cade2.pulse.data.repository

import com.cade2.pulse.data.remote.api.InsightsApi
import com.cade2.pulse.domain.model.CalendarDay
import com.cade2.pulse.domain.model.EmotionCard
import com.cade2.pulse.domain.model.EmotionFrequency
import com.cade2.pulse.domain.model.Pattern
import com.cade2.pulse.domain.model.WeeklyInsight
import com.cade2.pulse.util.Result
import javax.inject.Inject

interface InsightsRepository {
    suspend fun getWeeklyInsights(): Result<WeeklyInsight>
    suspend fun getPatterns(): Result<List<Pattern>>
    suspend fun getCalendar(month: Int, year: Int): Result<List<CalendarDay>>
}

class InsightsRepositoryImpl @Inject constructor(
    private val insightsApi: InsightsApi
) : InsightsRepository {

    override suspend fun getWeeklyInsights(): Result<WeeklyInsight> {
        return try {
            val response = insightsApi.getWeeklyInsights()
            Result.Success(
                WeeklyInsight(
                    weekStart = response.weekStart,
                    weekEnd = response.weekEnd,
                    totalSessions = response.totalSessions,
                    topEmotions = response.topEmotions.map {
                        EmotionFrequency(
                            card = EmotionCard(
                                it.card.cardId,
                                it.card.name,
                                it.card.description,
                                it.card.emoji,
                                it.card.category
                            ),
                            count = it.count,
                            percentage = it.percentage
                        )
                    },
                    mostFrequentEmotion = response.mostFrequentEmotion?.let {
                        EmotionCard(it.cardId, it.name, it.description, it.emoji, it.category)
                    }
                )
            )
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to load weekly insights")
        }
    }

    override suspend fun getPatterns(): Result<List<Pattern>> {
        return try {
            val response = insightsApi.getPatterns()
            Result.Success(response.map { Pattern(it.patternId, it.emoji, it.title, it.description) })
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to load patterns")
        }
    }

    override suspend fun getCalendar(month: Int, year: Int): Result<List<CalendarDay>> {
        return try {
            val response = insightsApi.getCalendar(month, year)
            Result.Success(response.days.map { CalendarDay(it.date, it.status, it.dominantSentiment) })
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to load calendar")
        }
    }
}
