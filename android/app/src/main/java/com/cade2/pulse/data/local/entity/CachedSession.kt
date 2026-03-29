package com.cade2.pulse.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class CachedSession(
    @PrimaryKey
    val sessionId: String,
    val date: String,
    val status: String,
    val selectedCardIdsJson: String,
    val contextSocial: String?,
    val contextEnergy: String?,
    val contextSleep: String?,
    val synced: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
