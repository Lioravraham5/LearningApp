package com.example.learningapp.lessonDetails

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt module that tells the dependency injection framework how to provide
 * instances related to the Lesson feature.
 */
@Module
@InstallIn(SingletonComponent::class) // This makes the repository available app-wide
abstract class LessonDetailsModule {

    /**
     * Binds the interface to our implementation.
     * * BEST PRACTICE: Currently pointing to the Mock implementation for UI development.
     * When the backend is ready, simply change `MockLessonRepositoryImpl`
     * to `RemoteLessonRepositoryImpl` right here, and the entire app will use real data!
     */
    @Binds
    abstract fun bindLessonDetailsRepository(
        impl: RemoteLessonDetailsRepositoryImpl
    ): LessonDetailsRepository
}