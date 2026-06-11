package com.example.learningapp.lessonProgress

import android.util.Log
import com.example.learningapp.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.example.learningapp.lessonProgress.models.AssessmentResponse
import com.example.learningapp.lessonProgress.models.LessonStartResponse
import com.example.learningapp.lessonProgress.models.Sentence
import java.io.File
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Concrete implementation of [LessonProgressRepository] that uses Retrofit to communicate with the server.
 */
class LessonProgressRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : LessonProgressRepository {

    private val TAG = "LessonProgressRepositoryImpl"

    override suspend fun startLesson(lessonId: String, isResume: Boolean): Result<LessonStartResponse> {
        return withContext(Dispatchers.IO) {
            try {
                // Log whether we are starting fresh or resuming
                Log.d(TAG, "Requesting run_id for lesson: $lessonId, isResume: $isResume")

                // Pass the isResume flag to the Retrofit ApiService
                val response = apiService.startLesson(lessonId, isResume)
                Result.success(response)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to start lesson and retrieve run_id", e)
                Result.failure(e)
            }
        }
    }

    override suspend fun getLessonSentences(lessonId: String): Result<List<Sentence>> {
        // Force data operations to run on the IO Dispatcher.
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Fetching sentences for lesson: $lessonId")
                val sentences = apiService.getLessonSentences(lessonId)
                Result.success(sentences)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to fetch sentences", e)
                Result.failure(e)
            }
        }
    }

    override suspend fun evaluateSpeech(
        audioFile: File,
        sentenceId: String,
        runId: String, // Active session tracking ID
        language: String?
    ): Result<AssessmentResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Preparing to upload audio file. Size: ${audioFile.length()} bytes")

                // 1. Prepare the Audio File Part
                val requestFile = audioFile.asRequestBody("audio/mp4".toMediaTypeOrNull())
                val filePart = MultipartBody.Part.createFormData("file", audioFile.name, requestFile)

                // 2. Prepare the Text Fields
                val sentenceIdPart = sentenceId.toRequestBody("text/plain".toMediaTypeOrNull())
                val runIdPart = runId.toRequestBody("text/plain".toMediaTypeOrNull())
                val languagePart = language?.toRequestBody("text/plain".toMediaTypeOrNull())

                // 3. Execute the Network Request
                Log.d(TAG, "Sending audio to server for evaluation under runId: $runId...")
                val evaluationResult = apiService.evaluateAudio(
                    file = filePart,
                    sentenceId = sentenceIdPart,
                    runId = runIdPart, // Include runId in the Multipart request
                    language = languagePart
                )

                Log.d(TAG, "Evaluation received successfully! Score: ${evaluationResult.finalScore}")
                Result.success(evaluationResult)

            } catch (e: Exception) {
                Log.e(TAG, "Failed to evaluate speech", e)
                Result.failure(e)
            }
        }
    }
}