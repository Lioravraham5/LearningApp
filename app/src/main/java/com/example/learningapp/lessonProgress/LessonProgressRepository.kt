package com.example.learningapp.lessonProgress

import com.example.learningapp.lessonProgress.models.AssessmentResponse
import com.example.learningapp.lessonProgress.models.LessonStartResponse
import com.example.learningapp.lessonProgress.models.Sentence
import java.io.File

/**
 * Interface defining the data operations for the Interactive Lesson feature.
 */
interface LessonProgressRepository {

    /**
     * Initializes a new run for a specific lesson or resumes an existing one.
     * MUST be called once when the user starts or restarts a lesson to get a unique run_id.
     * @param lessonId The ID of the lesson to start.
     * @param isResume True if the user is resuming an in-progress lesson, False to start fresh.
     * @return A [Result] containing the [LessonStartResponse] (which holds the runId) on success, or an Exception on failure.
     */
    suspend fun startLesson(lessonId: String, isResume: Boolean = false): Result<LessonStartResponse>

    /**
     * Fetches the sequence of sentences for a given lesson.
     * @param lessonId The ID of the lesson to fetch.
     * @return A [Result] containing the list of sentences on success, or an Exception on failure.
     */
    suspend fun getLessonSentences(lessonId: String): Result<List<Sentence>>

    /**
     * Uploads the user's audio recording and evaluates their pronunciation.
     * @param audioFile The locally saved .m4a file containing the user's voice.
     * @param sentenceId The unique ID of the exact sentence the user was asked to read.
     * @param runId The active session UUID tracking the user's progress.
     * @param language Optional language code (e.g., "en-US") to help the ASR model.
     * @return A [Result] containing the Assessment response on success, or an Exception on failure.
     */
    suspend fun evaluateSpeech(
        audioFile: File,
        sentenceId: String,
        runId: String,
        language: String? = "en-US"
    ): Result<AssessmentResponse>
}