package com.cade2.pulse.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.cade2.pulse.data.local.entity.CachedSwipe
import kotlinx.coroutines.flow.Flow

@Dao
interface SwipeDao {

    @Query("SELECT * FROM swipes WHERE sessionId = :sessionId")
    fun getSwipesForSession(sessionId: String): Flow<List<CachedSwipe>>

    @Query("SELECT * FROM swipes WHERE sessionId = :sessionId")
    suspend fun getSwipesForSessionList(sessionId: String): List<CachedSwipe>

    @Query("SELECT * FROM swipes WHERE sessionId = :sessionId AND accepted = 1")
    suspend fun getAcceptedSwipesForSession(sessionId: String): List<CachedSwipe>

    @Upsert
    suspend fun upsertSwipe(swipe: CachedSwipe)

    @Query("DELETE FROM swipes WHERE sessionId = :sessionId")
    suspend fun deleteSwipesForSession(sessionId: String)

    @Query("DELETE FROM swipes")
    suspend fun clearAll()
}
