package com.cade2.pulse.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.cade2.pulse.data.local.entity.CachedSession
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Query("SELECT * FROM sessions ORDER BY createdAt DESC")
    fun getAllSessions(): Flow<List<CachedSession>>

    @Query("SELECT * FROM sessions WHERE date = :date LIMIT 1")
    suspend fun getSessionByDate(date: String): CachedSession?

    @Query("SELECT * FROM sessions WHERE synced = 0")
    suspend fun getUnsyncedSessions(): List<CachedSession>

    @Upsert
    suspend fun upsertSession(session: CachedSession)

    @Query("UPDATE sessions SET synced = 1 WHERE sessionId = :sessionId")
    suspend fun markAsSynced(sessionId: String)

    @Query("DELETE FROM sessions")
    suspend fun clearAll()

    @Query("SELECT * FROM sessions ORDER BY date DESC LIMIT :limit")
    suspend fun getRecentSessions(limit: Int): List<CachedSession>
}
