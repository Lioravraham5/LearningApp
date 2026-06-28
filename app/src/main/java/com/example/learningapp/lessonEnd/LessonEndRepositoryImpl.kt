package com.example.learningapp.lessonEnd

import android.util.Log
import com.example.learningapp.lessonEnd.models.LessonCompleteRequest
import com.example.learningapp.lessonEnd.models.LessonCompleteResponse
import com.example.learningapp.network.ApiService
import javax.inject.Inject

/**
 * Real implementation of the [LessonEndRepository] that communicates with the FastAPI server.
 */
class LessonEndRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : LessonEndRepository {

    private val TAG = "LessonEndRepository"

    override suspend fun completeLesson(lessonId: String, runId: String): Result<LessonCompleteResponse> {
        return try {
            // BEST PRACTICE: We construct the Request Body object here, keeping the ViewModel clean and completely unaware of network-specific schemas.
            val request = LessonCompleteRequest(runId = runId)

            // Execute the network call
            val response = apiService.completeLesson(lessonId = lessonId, request = request)

            // Log success for debugging and return the data
            Log.d(TAG, "Successfully completed lesson: $lessonId with score: ${response.averageScore}")
            Result.success(response)

        } catch (e: Exception) {
            // Log the error and safely pass it up the chain without crashing the app
            Log.e(TAG, "Failed to complete lesson: $lessonId. Error: ${e.message}", e)
            Result.failure(e)
        }
    }
}