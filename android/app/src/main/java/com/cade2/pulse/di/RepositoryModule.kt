package com.cade2.pulse.di

import com.cade2.pulse.data.repository.AuthRepository
import com.cade2.pulse.data.repository.AuthRepositoryImpl
import com.cade2.pulse.data.repository.InsightsRepository
import com.cade2.pulse.data.repository.InsightsRepositoryImpl
import com.cade2.pulse.data.repository.SessionRepository
import com.cade2.pulse.data.repository.SessionRepositoryImpl
import com.cade2.pulse.data.repository.UserRepository
import com.cade2.pulse.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindSessionRepository(impl: SessionRepositoryImpl): SessionRepository

    @Binds
    @Singleton
    abstract fun bindInsightsRepository(impl: InsightsRepositoryImpl): InsightsRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}
