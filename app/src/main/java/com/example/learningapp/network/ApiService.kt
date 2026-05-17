package com.example.learningapp.network

import com.example.learningapp.categoryDetails.CategoryDetails
import com.example.learningapp.home.Category
import com.example.learningapp.lessonDetails.LessonDetails
import com.example.learningapp.lessonProgress.models.ASRCombinedOut
import com.example.learningapp.lessonProgress.models.Sentence
import com.example.learningapp.progress.Badge
import com.example.learningapp.progress.CategoryAchievement
import com.example.learningapp.progress.OverviewData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

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
     * Fetches all sentences for a specific lesson to start the interactive session.
     */
    @GET("lessons/{lesson_id}/sentences")
    suspend fun getLessonSentences(
        @Path("lesson_id") lessonId: String
    ): List<Sentence>

    /**
     * Uploads the user's recorded audio and the target sentence to the server for evaluation.
     * BEST PRACTICE: Using @Multipart allows us to send the physical file (.m4a) alongside
     * regular text fields (target_sentence, language) efficiently.
     *
     * @param file The recorded audio file wrapped in a MultipartBody.Part
     * @param targetSentence The exact sentence the user was supposed to read
     * @param language Optional expected language code (e.g., "en")
     */
    @Multipart
    @POST("asr")
    suspend fun evaluateAudio(
        @Part file: MultipartBody.Part,
        @Part("target_sentence") targetSentence: RequestBody,
        @Part("language") language: RequestBody? = null
    ): ASRCombinedOut

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