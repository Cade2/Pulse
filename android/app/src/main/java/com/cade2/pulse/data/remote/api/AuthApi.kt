package com.cade2.pulse.data.remote.api

import com.cade2.pulse.data.remote.dto.AuthResponseDto
import com.cade2.pulse.data.remote.dto.FcmTokenRequestDto
import com.cade2.pulse.data.remote.dto.LoginRequestDto
import com.cade2.pulse.data.remote.dto.RefreshRequestDto
import com.cade2.pulse.data.remote.dto.RefreshResponseDto
import com.cade2.pulse.data.remote.dto.RegisterRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequestDto): AuthResponseDto

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequestDto): AuthResponseDto

    @POST("api/auth/refresh")
    suspend fun refreshToken(@Body request: RefreshRequestDto): RefreshResponseDto

    @POST("api/auth/fcm-token")
    suspend fun updateFcmToken(@Body request: FcmTokenRequestDto)
}
