package com.cade2.pulse.data.remote.api

import com.cade2.pulse.data.remote.dto.CalendarResponseDto
import com.cade2.pulse.data.remote.dto.PatternDto
import com.cade2.pulse.data.remote.dto.WeeklyInsightDto
import retrofit2.http.GET
import retrofit2.http.Query

interface InsightsApi {

    @GET("api/insights/weekly")
    suspend fun getWeeklyInsights(): WeeklyInsightDto

    @GET("api/insights/patterns")
    suspend fun getPatterns(): List<PatternDto>

    @GET("api/insights/calendar")
    suspend fun getCalendar(
        @Query("month") month: Int,
        @Query("year") year: Int
    ): CalendarResponseDto
}
