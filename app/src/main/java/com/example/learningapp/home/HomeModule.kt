package com.example.learningapp.home

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt module for the Home feature.
 */
@Module
@InstallIn(SingletonComponent::class) // Tells Hilt that the bindings here are available throughout the app's lifecycle
abstract class HomeModule {

    /**
     * Binds the implementation of HomeRepository to its interface.
     * Whenever a class (like HomeViewModel) requests a HomeRepository as a dependency,
     * Hilt will provide an instance of MockHomeRepositoryImpl.
     */
    @Binds
    abstract fun bindHomeRepository(
        homeRepositoryImpl: MockHomeRepositoryImpl
    ): HomeRepository
}