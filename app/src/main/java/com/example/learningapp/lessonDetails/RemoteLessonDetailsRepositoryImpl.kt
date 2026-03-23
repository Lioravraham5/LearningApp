package com.example.learningapp.lessonDetails

import com.example.learningapp.network.ApiService
import javax.inject.Inject

/**
 * Remote implementation of [LessonDetailsRepository].
 * Fetches real lesson details from the backend server (e.g., FastAPI) using Retrofit.
 * @GET("lessons/{lessonId}")
 * suspend fun getLessonDetails(@Path("lessonId") lessonId: String): LessonDetails
 */
class RemoteLessonDetailsRepositoryImpl @Inject constructor(
    private val apiService: ApiService // Assuming ApiService is already provided by a NetworkModule
) : LessonDetailsRepository {

    override suspend fun getLessonDetails(lessonId: String): LessonDetails {
        // The AuthInterceptor will automatically attach the user's Firebase token.
        return apiService.getLessonDetails(lessonId)
    }
}