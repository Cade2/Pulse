package com.cade2.pulse.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "swipes")
data class CachedSwipe(
    @PrimaryKey
    val swipeId: String,
    val sessionId: String,
    val cardId: String,
    val accepted: Boolean,
    val swipedAt: Long = System.currentTimeMillis()
)
