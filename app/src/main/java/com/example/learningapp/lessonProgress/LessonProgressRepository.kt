package com.example.learningapp.lessonProgress

import com.example.learningapp.lessonProgress.models.ASRCombinedOut
import com.example.learningapp.lessonProgress.models.Sentence
import java.io.File

/**
 * Interface defining the data operations for the Interactive Lesson feature.
 */
interface LessonProgressRepository {

    /**
     * Fetches the sequence of sentences for a given lesson.
     * @param lessonId The ID of the lesson to fetch.
     * @return A [Result] containing the list of sentences on success, or an Exception on failure.
     */
    suspend fun getLessonSentences(lessonId: String): Result<List<Sentence>>

    /**
     * Uploads the user's audio recording and evaluates their pronunciation.
     * @param audioFile The locally saved .m4a file containing the user's voice.
     * @param targetSentence The exact sentence the user was asked to read.
     * @param language Optional language code (e.g., "en") to help the ASR model.
     * @return A [Result] containing the LLM evaluation on success, or an Exception on failure.
     */
    suspend fun evaluateSpeech(
        audioFile: File,
        targetSentence: String,
        language: String? = "en"
    ): Result<ASRCombinedOut>
}