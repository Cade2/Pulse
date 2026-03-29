package com.cade2.pulse.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.cade2.pulse.data.local.entity.CachedEmotionCard
import kotlinx.coroutines.flow.Flow

@Dao
interface EmotionCardDao {

    @Query("SELECT * FROM emotion_cards ORDER BY name ASC")
    fun getAllCards(): Flow<List<CachedEmotionCard>>

    @Query("SELECT * FROM emotion_cards ORDER BY name ASC")
    suspend fun getAllCardsList(): List<CachedEmotionCard>

    @Query("SELECT * FROM emotion_cards WHERE cardId = :cardId")
    suspend fun getCardById(cardId: String): CachedEmotionCard?

    @Query("SELECT * FROM emotion_cards WHERE category = :category")
    fun getCardsByCategory(category: String): Flow<List<CachedEmotionCard>>

    @Upsert
    suspend fun upsertCards(cards: List<CachedEmotionCard>)

    @Upsert
    suspend fun upsertCard(card: CachedEmotionCard)

    @Query("DELETE FROM emotion_cards")
    suspend fun clearAll()

    @Query("SELECT cachedAt FROM emotion_cards ORDER BY cachedAt DESC LIMIT 1")
    suspend fun getLastCachedAt(): Long?
}
