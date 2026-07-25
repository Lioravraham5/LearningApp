package com.example.learningapp.progress.di

import com.example.learningapp.progress.data.ProgressRepository
import com.example.learningapp.progress.data.RemoteProgressRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt module for the Progress feature.
 */
@Module
@InstallIn(SingletonComponent::class) // Available throughout the app's lifecycle
abstract class ProgressModule {

    /**
     * Binds the implementation of ProgressRepository to its interface.
     * Whenever a class (like ProgressViewModel) requests a ProgressRepository as a dependency,
     * Hilt will provide an instance of MockProgressRepositoryImpl.
     */
    @Binds
    abstract fun bindProgressRepository(
        mockProgressRepositoryImpl: RemoteProgressRepositoryImpl
    ): ProgressRepository
}