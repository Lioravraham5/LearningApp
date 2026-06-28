package com.example.learningapp.lessonEnd

import com.example.learningapp.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides dependencies specifically for the Lesson End feature.
 */
@Module
@InstallIn(SingletonComponent::class)
object LessonEndModule {

    /**
     * Provides the repository responsible for finalizing the lesson data operations.
     * BEST PRACTICE: By explicitly binding the [LessonEndRepositoryImpl] to the [LessonEndRepository] interface,
     * we instruct Hilt to inject the implementation whenever the interface is requested.
     */
    @Provides
    @Singleton
    fun provideLessonEndRepository(
        apiService: ApiService
    ): LessonEndRepository {
        return LessonEndRepositoryImpl(apiService)
    }
}