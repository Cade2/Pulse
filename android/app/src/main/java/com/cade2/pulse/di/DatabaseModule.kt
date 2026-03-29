package com.cade2.pulse.di

import android.content.Context
import androidx.room.Room
import com.cade2.pulse.data.local.PulseDatabase
import com.cade2.pulse.data.local.dao.EmotionCardDao
import com.cade2.pulse.data.local.dao.SessionDao
import com.cade2.pulse.data.local.dao.SwipeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePulseDatabase(@ApplicationContext context: Context): PulseDatabase {
        return Room.databaseBuilder(
            context,
            PulseDatabase::class.java,
            "pulse_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideEmotionCardDao(db: PulseDatabase): EmotionCardDao = db.emotionCardDao()

    @Provides
    @Singleton
    fun provideSessionDao(db: PulseDatabase): SessionDao = db.sessionDao()

    @Provides
    @Singleton
    fun provideSwipeDao(db: PulseDatabase): SwipeDao = db.swipeDao()
}
