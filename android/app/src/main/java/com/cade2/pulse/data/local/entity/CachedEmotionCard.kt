package com.cade2.pulse.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emotion_cards")
data class CachedEmotionCard(
    @PrimaryKey
    val cardId: String,
    val name: String,
    val description: String,
    val emoji: String,
    val category: String,
    val cachedAt: Long = System.currentTimeMillis()
)
