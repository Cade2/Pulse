package com.cade2.pulse.data.remote.api

import com.cade2.pulse.data.remote.dto.CompleteSessionRequestDto
import com.cade2.pulse.data.remote.dto.CompleteSessionResponseDto
import com.cade2.pulse.data.remote.dto.SessionHistoryItemDto
import com.cade2.pulse.data.remote.dto.StreakResponseDto
import com.cade2.pulse.data.remote.dto.TodaySessionDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SessionApi {

    @GET("api/sessions/today")
    suspend fun getTodaySession(): TodaySessionDto

    @POST("api/sessions/complete")
    suspend fun completeSession(@Body request: CompleteSessionRequestDto): CompleteSessionResponseDto

    @GET("api/sessions/history")
    suspend fun getSessionHistory(): List<SessionHistoryItemDto>

    @GET("api/sessions/streak")
    suspend fun getStreak(): StreakResponseDto
}
