package com.cade2.pulse.data.remote.api

import com.cade2.pulse.data.remote.dto.UpdateSettingsRequestDto
import com.cade2.pulse.data.remote.dto.UpdateSettingsResponseDto
import com.cade2.pulse.data.remote.dto.UserProfileDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserApi {

    @GET("api/user/profile")
    suspend fun getProfile(): UserProfileDto

    @PUT("api/user/settings")
    suspend fun updateSettings(@Body request: UpdateSettingsRequestDto): UpdateSettingsResponseDto

    @DELETE("api/user/account")
    suspend fun deleteAccount()
}
