package com.example.learningapp.lessonProgress

import android.content.Context
import com.example.learningapp.lessonProgress.services.AndroidAudioRecorderService
import com.example.learningapp.lessonProgress.services.AudioRecorderService
import com.example.learningapp.lessonProgress.services.AzureTtsService
import com.example.learningapp.lessonProgress.services.TtsService
import com.example.learningapp.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides dependencies for the Lesson feature.
 * BEST PRACTICE: We install this in the [SingletonComponent] because hardware resources
 * like the Microphone and Speech Synthesizer should have only ONE instance running
 * across the entire app to prevent overlapping audio or crash leaks.
 */
@Module
@InstallIn(SingletonComponent::class)
object LessonProgressModule {

    /**
     * Provides the Text-to-Speech service.
     * Whenever a class asks for a [TtsService], Hilt will give it our [AzureTtsService].
     */
    @Provides
    @Singleton
    fun provideTtsService(): TtsService {
        return AzureTtsService()
    }

    /**
     * Provides the Audio Recording service.
     * BEST PRACTICE: We inject the @ApplicationContext so the recorder survives
     * screen rotations (configuration changes) without causing memory leaks.
     */
    @Provides
    @Singleton
    fun provideAudioRecorderService(
        @ApplicationContext context: Context
    ): AudioRecorderService {
        return AndroidAudioRecorderService(context)
    }

    /**
     * Provides the repository responsible for the Interactive Lesson's data operations.
     * BEST PRACTICE: By providing the interface [LessonProgressRepository], we hide the
     * actual implementation ([LessonProgressRepositoryImpl]) from the ViewModels.
     * Hilt automatically resolves and injects the required [ApiService] parameter here.
     */
    @Provides
    @Singleton
    fun provideLessonProgressRepository(
        apiService: ApiService
    ): LessonProgressRepository {
        return LessonProgressRepositoryImpl(apiService)
    }
}