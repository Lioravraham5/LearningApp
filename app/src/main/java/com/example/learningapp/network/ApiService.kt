package com.example.learningapp.network

import com.example.learningapp.categoryDetails.CategoryDetails
import com.example.learningapp.home.Category
import com.example.learningapp.progress.Badge
import com.example.learningapp.progress.CategoryAchievement
import com.example.learningapp.progress.OverviewData
import retrofit2.http.GET
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
    // PROGRESS ENDPOINTS
    // ==========================================

    @GET("progress/overview")
    suspend fun getOverviewData(): OverviewData

    @GET("progress/categories")
    suspend fun getCategoryAchievements(): List<CategoryAchievement>

    @GET("progress/badges")
    suspend fun getBadges(): List<Badge>
}