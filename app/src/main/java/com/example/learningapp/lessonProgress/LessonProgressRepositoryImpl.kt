package com.example.learningapp.lessonProgress

import android.util.Log
import com.example.learningapp.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.example.learningapp.lessonProgress.models.AssessmentResponse
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

    override suspend fun getLessonSentences(lessonId: String): Result<List<Sentence>> {
        // Force data operations to run on the IO Dispatcher. This ensures the Main (UI) thread is never blocked during network requests.
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
        language: String?
    ): Result<AssessmentResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Preparing to upload audio file. Size: ${audioFile.length()} bytes")

                // 1. Prepare the Audio File Part
                // We use "audio/mp4" as the media type because we saved it as an MPEG_4 container (.m4a)
                val requestFile = audioFile.asRequestBody("audio/mp4".toMediaTypeOrNull())
                val filePart = MultipartBody.Part.createFormData("file", audioFile.name, requestFile)

                // 2. Prepare the Text Fields
                // Sending the sentenceId instead of the target sentence text
                val sentenceIdPart = sentenceId.toRequestBody("text/plain".toMediaTypeOrNull())
                val languagePart = language?.toRequestBody("text/plain".toMediaTypeOrNull())

                // 3. Execute the Network Request
                Log.d(TAG, "Sending audio to server for evaluation...")
                val evaluationResult = apiService.evaluateAudio(
                    file = filePart,
                    sentenceId = sentenceIdPart,
                    language = languagePart
                )

                // Log using the new 'finalScore' property from AssessmentResponse
                Log.d(TAG, "Evaluation received successfully! Score: ${evaluationResult.finalScore}")
                Result.success(evaluationResult)

            } catch (e: Exception) {
                Log.e(TAG, "Failed to evaluate speech", e)
                Result.failure(e)
            }
        }
    }
}