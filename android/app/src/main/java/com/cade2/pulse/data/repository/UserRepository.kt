package com.cade2.pulse.data.repository

import com.cade2.pulse.data.local.dao.EmotionCardDao
import com.cade2.pulse.data.local.dao.SessionDao
import com.cade2.pulse.data.local.dao.SwipeDao
import com.cade2.pulse.data.remote.api.UserApi
import com.cade2.pulse.data.remote.dto.UpdateSettingsRequestDto
import com.cade2.pulse.domain.model.User
import com.cade2.pulse.util.EncryptedPrefs
import com.cade2.pulse.util.Result
import javax.inject.Inject

interface UserRepository {
    suspend fun getProfile(): Result<User>
    suspend fun updateSettings(avatarColor: String?, notificationHour: Int, notificationMinute: Int): Result<Unit>
    suspend fun deleteAccount(): Result<Unit>
    suspend fun logout()
}

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val encryptedPrefs: EncryptedPrefs,
    private val sessionDao: SessionDao,
    private val swipeDao: SwipeDao,
    private val emotionCardDao: EmotionCardDao
) : UserRepository {

    override suspend fun getProfile(): Result<User> {
        return try {
            val response = userApi.getProfile()
            Result.Success(
                User(
                    userId = response.userId,
                    name = response.name,
                    email = response.email,
                    avatarColor = response.avatarColor ?: "#A78BFA",
                    notificationHour = response.notificationHour,
                    notificationMinute = response.notificationMinute,
                    memberSince = response.memberSince
                )
            )
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to load profile")
        }
    }

    override suspend fun updateSettings(
        avatarColor: String?,
        notificationHour: Int,
        notificationMinute: Int
    ): Result<Unit> {
        return try {
            userApi.updateSettings(UpdateSettingsRequestDto(avatarColor, notificationHour, notificationMinute))
            avatarColor?.let { encryptedPrefs.avatarColor = it }
            encryptedPrefs.notificationHour = notificationHour
            encryptedPrefs.notificationMinute = notificationMinute
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to update settings")
        }
    }

    override suspend fun deleteAccount(): Result<Unit> {
        return try {
            userApi.deleteAccount()
            logout()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to delete account")
        }
    }

    override suspend fun logout() {
        encryptedPrefs.clearAll()
        sessionDao.clearAll()
        swipeDao.clearAll()
        emotionCardDao.clearAll()
    }
}
