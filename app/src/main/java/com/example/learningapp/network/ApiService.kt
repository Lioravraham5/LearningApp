package com.example.learningapp.network

import com.example.learningapp.categoryDetails.CategoryDetails
import com.example.learningapp.home.Category
import com.example.learningapp.lessonDetails.LessonDetails
import com.example.learningapp.lessonProgress.models.AssessmentResponse
import com.example.learningapp.lessonProgress.models.Sentence
import com.example.learningapp.progress.Badge
import com.example.learningapp.progress.CategoryAchievement
import com.example.learningapp.progress.OverviewData
import com.example.learningapp.lessonProgress.models.LessonStartResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * REST API definition for the AI Avatar Learning App.
 * * BEST PRACTICE: We define suspended functions so Retrofit works seamlessly
 * with Kotlin Coroutines, executing network calls off the main thread automatically.
 */
interface ApiService {

    // ==========================================
    // CATEGORIES ENDPOINTS
    // ==========================================

    @GET("categories/")
    suspend fun getCategories(): List<Category>

    @GET("categories/{category_id}")
    suspend fun getCategoryDetails(
        @Path("category_id") categoryId: String
    ): CategoryDetails

    // ==========================================
    // LESSONS ENDPOINTS
    // ==========================================

    @GET("lessons/{lesson_id}")
    suspend fun getLessonDetails(
        @Path("lesson_id") lessonId: String
    ): LessonDetails

    // ==========================================
    // LESSON PROGRESS ENDPOINTS
    // ==========================================

    /**
     * Initializes a new run for a specific lesson.
     * MUST be called once when the user starts or restarts a lesson to get a unique run_id.
     */
    @POST("lessons/{lesson_id}/start")
    suspend fun startLesson(
        @Path("lesson_id") lessonId: String,
        @Query("is_resume") isResume: Boolean
    ): LessonStartResponse

    /**
     * Fetches all sentences for a specific lesson to start the interactive session.
     */
    @GET("lessons/{lesson_id}/sentences")
    suspend fun getLessonSentences(
        @Path("lesson_id") lessonId: String
    ): List<Sentence>

    /**
     * Uploads the user's recorded audio alongside the target sentence ID and the active run_id.
     *
     * @param file The recorded audio file wrapped in a MultipartBody.Part.
     * @param sentenceId The unique identifier of the target sentence.
     * @param runId The active session UUID tracking the user's progress.
     * @param language Optional expected language code (e.g., "en-US").
     */
    @Multipart
    @POST("asr")
    suspend fun evaluateAudio(
        @Part file: MultipartBody.Part,
        @Part("sentence_id") sentenceId: RequestBody,
        @Part("run_id") runId: RequestBody,
        @Part("language") language: RequestBody? = null
    ): AssessmentResponse

    // ==========================================
    // PROGRESS ENDPOINTS
    // ==========================================

    @GET("progress/overview")
    suspend fun getOverviewData(): OverviewData

    @GET("progress/categories")
    suspend fun getCategoryAchievements(): List<CategoryAchievement>

    @GET("progress/badges")
    suspend fun getBadges(): List<Badge>
}