package com.example.learningapp.lessonEnd

import com.example.learningapp.lessonEnd.models.LessonCompleteResponse

/**
 * Interface defining the data operations for the Lesson End feature.
 */
interface LessonEndRepository {

    /**
     * Finalizes the lesson run in the backend and fetches the summary metrics.
     * * @param lessonId The unique ID of the lesson.
     * @param runId The active session UUID tracking the user's progress.
     * @return A [Result] containing the [com.example.learningapp.lessonEnd.models.LessonCompleteResponse] on success, or an Exception on failure.
     */
    suspend fun completeLesson(lessonId: String, runId: String): Result<LessonCompleteResponse>
}