package com.cade2.pulse.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cade2.pulse.data.local.dao.EmotionCardDao
import com.cade2.pulse.data.local.dao.SessionDao
import com.cade2.pulse.data.local.dao.SwipeDao
import com.cade2.pulse.data.local.entity.CachedEmotionCard
import com.cade2.pulse.data.local.entity.CachedSession
import com.cade2.pulse.data.local.entity.CachedSwipe

@Database(
    entities = [
        CachedEmotionCard::class,
        CachedSession::class,
        CachedSwipe::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PulseDatabase : RoomDatabase() {
    abstract fun emotionCardDao(): EmotionCardDao
    abstract fun sessionDao(): SessionDao
    abstract fun swipeDao(): SwipeDao
}
