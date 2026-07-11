package com.taskflow.app.di

import com.taskflow.app.data.repository.PreferencesRepositoryImpl
import com.taskflow.app.data.repository.TaskRepositoryImpl
import com.taskflow.app.domain.repository.PreferencesRepository
import com.taskflow.app.domain.repository.TaskRepository
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
    abstract fun bindTaskRepository(
        impl: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindPreferencesRepository(
        impl: PreferencesRepositoryImpl
    ): PreferencesRepository
}
