package com.cade2.pulse.data.repository

import com.cade2.pulse.data.local.dao.EmotionCardDao
import com.cade2.pulse.data.local.dao.SessionDao
import com.cade2.pulse.data.local.dao.SwipeDao
import com.cade2.pulse.data.local.entity.CachedEmotionCard
import com.cade2.pulse.data.local.entity.CachedSession
import com.cade2.pulse.data.local.entity.CachedSwipe
import com.cade2.pulse.data.remote.api.SessionApi
import com.cade2.pulse.data.remote.dto.CompleteSessionRequestDto
import com.cade2.pulse.domain.model.EmotionCard
import com.cade2.pulse.domain.model.Session
import com.cade2.pulse.domain.model.Streak
import com.cade2.pulse.util.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

interface SessionRepository {
    suspend fun getTodaySession(): Result<Session?>
    suspend fun getEmotionCards(): Result<List<EmotionCard>>
    suspend fun saveSwipe(sessionId: String, cardId: String, accepted: Boolean): Result<Unit>
    suspend fun completeSession(
        sessionId: String,
        acceptedCardIds: List<String>,
        contextSocial: String?,
        contextEnergy: String?,
        contextSleep: String?
    ): Result<Int>
    suspend fun getSessionHistory(): Result<List<Session>>
    suspend fun getStreak(): Result<Streak>
    suspend fun syncUnsyncedSessions(): Result<Unit>
}

class SessionRepositoryImpl @Inject constructor(
    private val sessionApi: SessionApi,
    private val sessionDao: SessionDao,
    private val swipeDao: SwipeDao,
    private val emotionCardDao: EmotionCardDao
) : SessionRepository {

    private val gson = Gson()

    override suspend fun getTodaySession(): Result<Session?> {
        return try {
            val response = sessionApi.getTodaySession()
            val cards = response.cards.map {
                CachedEmotionCard(it.cardId, it.name, it.description, it.emoji, it.category)
            }
            emotionCardDao.upsertCards(cards)

            val session = if (response.sessionId != null) {
                val acceptedEmotions = response.acceptedCardIds
                    ?.mapNotNull { id -> cards.find { it.cardId == id }?.toDomain() }
                    ?: emptyList()
                Session(
                    sessionId = response.sessionId,
                    date = response.date,
                    status = response.status,
                    acceptedEmotions = acceptedEmotions,
                    contextSocial = response.contextSocial,
                    contextEnergy = response.contextEnergy,
                    contextSleep = response.contextSleep
                )
            } else null
            Result.Success(session)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to load today's session")
        }
    }

    override suspend fun getEmotionCards(): Result<List<EmotionCard>> {
        return try {
            val cached = emotionCardDao.getAllCardsList()
            if (cached.isNotEmpty()) {
                Result.Success(cached.map { it.toDomain() })
            } else {
                Result.Error("No cards available offline")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to load cards")
        }
    }

    override suspend fun saveSwipe(sessionId: String, cardId: String, accepted: Boolean): Result<Unit> {
        return try {
            swipeDao.upsertSwipe(
                CachedSwipe(
                    swipeId = UUID.randomUUID().toString(),
                    sessionId = sessionId,
                    cardId = cardId,
                    accepted = accepted
                )
            )
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to save swipe")
        }
    }

    override suspend fun completeSession(
        sessionId: String,
        acceptedCardIds: List<String>,
        contextSocial: String?,
        contextEnergy: String?,
        contextSleep: String?
    ): Result<Int> {
        return try {
            val response = sessionApi.completeSession(
                CompleteSessionRequestDto(sessionId, acceptedCardIds, contextSocial, contextEnergy, contextSleep)
            )
            sessionDao.markAsSynced(sessionId)
            Result.Success(response.streakCount)
        } catch (e: Exception) {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            sessionDao.upsertSession(
                CachedSession(
                    sessionId = sessionId,
                    date = today,
                    status = "completed",
                    selectedCardIdsJson = gson.toJson(acceptedCardIds),
                    contextSocial = contextSocial,
                    contextEnergy = contextEnergy,
                    contextSleep = contextSleep,
                    synced = false
                )
            )
            Result.Error(e.message ?: "Saved locally, will sync when online")
        }
    }

    override suspend fun getSessionHistory(): Result<List<Session>> {
        return try {
            val response = sessionApi.getSessionHistory()
            Result.Success(response.map { item ->
                Session(
                    sessionId = item.sessionId,
                    date = item.date,
                    status = item.status,
                    acceptedEmotions = item.acceptedEmotions.map { card ->
                        EmotionCard(card.cardId, card.name, card.description, card.emoji, card.category)
                    },
                    contextSocial = null,
                    contextEnergy = null,
                    contextSleep = null
                )
            })
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to load history")
        }
    }

    override suspend fun getStreak(): Result<Streak> {
        return try {
            val response = sessionApi.getStreak()
            Result.Success(Streak(response.currentStreak, response.longestStreak))
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to load streak")
        }
    }

    override suspend fun syncUnsyncedSessions(): Result<Unit> {
        return try {
            val unsynced = sessionDao.getUnsyncedSessions()
            unsynced.forEach { session ->
                val cardIds: List<String> = gson.fromJson(
                    session.selectedCardIdsJson,
                    object : TypeToken<List<String>>() {}.type
                )
                sessionApi.completeSession(
                    CompleteSessionRequestDto(
                        sessionId = session.sessionId,
                        acceptedCardIds = cardIds,
                        contextSocial = session.contextSocial,
                        contextEnergy = session.contextEnergy,
                        contextSleep = session.contextSleep
                    )
                )
                sessionDao.markAsSynced(session.sessionId)
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Sync failed")
        }
    }

    private fun CachedEmotionCard.toDomain() =
        EmotionCard(cardId, name, description, emoji, category)
}
