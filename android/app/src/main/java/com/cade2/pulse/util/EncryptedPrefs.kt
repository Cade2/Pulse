package com.cade2.pulse.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptedPrefs @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            Constants.PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    var accessToken: String?
        get() = prefs.getString(Constants.KEY_ACCESS_TOKEN, null)
        set(value) = prefs.edit().putString(Constants.KEY_ACCESS_TOKEN, value).apply()

    var refreshToken: String?
        get() = prefs.getString(Constants.KEY_REFRESH_TOKEN, null)
        set(value) = prefs.edit().putString(Constants.KEY_REFRESH_TOKEN, value).apply()

    var userName: String?
        get() = prefs.getString(Constants.KEY_USER_NAME, null)
        set(value) = prefs.edit().putString(Constants.KEY_USER_NAME, value).apply()

    var userId: String?
        get() = prefs.getString(Constants.KEY_USER_ID, null)
        set(value) = prefs.edit().putString(Constants.KEY_USER_ID, value).apply()

    var notificationHour: Int
        get() = prefs.getInt(Constants.KEY_NOTIFICATION_HOUR, 20)
        set(value) = prefs.edit().putInt(Constants.KEY_NOTIFICATION_HOUR, value).apply()

    var notificationMinute: Int
        get() = prefs.getInt(Constants.KEY_NOTIFICATION_MINUTE, 0)
        set(value) = prefs.edit().putInt(Constants.KEY_NOTIFICATION_MINUTE, value).apply()

    var avatarColor: String
        get() = prefs.getString(Constants.KEY_AVATAR_COLOR, "#A78BFA") ?: "#A78BFA"
        set(value) = prefs.edit().putString(Constants.KEY_AVATAR_COLOR, value).apply()

    fun isLoggedIn(): Boolean = !accessToken.isNullOrBlank()

    fun clearAll() {
        prefs.edit().clear().apply()
    }
}
