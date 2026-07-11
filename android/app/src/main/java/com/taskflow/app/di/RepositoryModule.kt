package com.taskflow.app.di

import com.taskflow.app.data.repository.ChatRepositoryImpl
import com.taskflow.app.data.repository.PreferencesRepositoryImpl
import com.taskflow.app.data.repository.TaskRepositoryImpl
import com.taskflow.app.domain.repository.ChatRepository
import com.taskflow.app.domain.repository.PreferencesRepository
import com.taskflow.app.domain.repository.TaskRepository
import com.taskflow.app.domain.service.AiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        impl: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindPreferencesRepository(
        impl: PreferencesRepositoryImpl
    ): PreferencesRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(
        impl: ChatRepositoryImpl
    ): ChatRepository

    companion object {
        @Provides
        @Singleton
        fun provideAiService(): AiService = AiService()
    }
}
