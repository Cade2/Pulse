package com.cade2.pulse.util

object Constants {
    const val BASE_URL = "https://api.pulseapp.io/"

    const val PREFS_NAME = "pulse_secure_prefs"
    const val KEY_ACCESS_TOKEN = "access_token"
    const val KEY_REFRESH_TOKEN = "refresh_token"
    const val KEY_USER_NAME = "user_name"
    const val KEY_USER_ID = "user_id"
    const val KEY_NOTIFICATION_HOUR = "notification_hour"
    const val KEY_NOTIFICATION_MINUTE = "notification_minute"
    const val KEY_AVATAR_COLOR = "avatar_color"

    const val NOTIFICATION_CHANNEL_ID = "pulse_daily_reminder"
    const val NOTIFICATION_CHANNEL_NAME = "Daily Reminder"
    const val NOTIFICATION_ID = 1001

    const val SYNC_WORK_TAG = "pulse_sync_work"
    const val NOTIFICATION_WORK_TAG = "pulse_notification_work"

    const val EMOTION_CARDS_PER_SESSION = 8
    const val SWIPE_THRESHOLD_DP = 120f
    const val MAX_ROTATION_DEGREES = 15f
    const val INSIGHTS_MIN_DAYS = 14

    val STREAK_MILESTONES = setOf(3, 7, 14, 30, 100)

    val AVATAR_COLOR_PRESETS = listOf(
        "#A78BFA",
        "#34D399",
        "#FB923C",
        "#60A5FA",
        "#F472B6",
        "#FBBF24"
    )
}
