package com.cade2.pulse.data.repository

import com.cade2.pulse.data.remote.api.AuthApi
import com.cade2.pulse.data.remote.dto.FcmTokenRequestDto
import com.cade2.pulse.data.remote.dto.LoginRequestDto
import com.cade2.pulse.data.remote.dto.RefreshRequestDto
import com.cade2.pulse.data.remote.dto.RegisterRequestDto
import com.cade2.pulse.util.EncryptedPrefs
import com.cade2.pulse.util.Result
import javax.inject.Inject

interface AuthRepository {
    suspend fun register(email: String, password: String, name: String): Result<Unit>
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun refreshToken(): Result<Unit>
    suspend fun updateFcmToken(token: String): Result<Unit>
}

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val encryptedPrefs: EncryptedPrefs
) : AuthRepository {

    override suspend fun register(email: String, password: String, name: String): Result<Unit> {
        return try {
            val response = authApi.register(RegisterRequestDto(email, password, name))
            encryptedPrefs.accessToken = response.accessToken
            encryptedPrefs.refreshToken = response.refreshToken
            encryptedPrefs.userId = response.userId
            encryptedPrefs.userName = response.name
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Registration failed")
        }
    }

    override suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            val response = authApi.login(LoginRequestDto(email, password))
            encryptedPrefs.accessToken = response.accessToken
            encryptedPrefs.refreshToken = response.refreshToken
            encryptedPrefs.userId = response.userId
            encryptedPrefs.userName = response.name
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Login failed")
        }
    }

    override suspend fun refreshToken(): Result<Unit> {
        val token = encryptedPrefs.refreshToken ?: return Result.Error("No refresh token")
        return try {
            val response = authApi.refreshToken(RefreshRequestDto(token))
            encryptedPrefs.accessToken = response.accessToken
            encryptedPrefs.refreshToken = response.refreshToken
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Token refresh failed")
        }
    }

    override suspend fun updateFcmToken(token: String): Result<Unit> {
        return try {
            authApi.updateFcmToken(FcmTokenRequestDto(token))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to update FCM token")
        }
    }
}
