package com.example.learningapp.categoryDetails

import com.example.learningapp.network.ApiService
import javax.inject.Inject

/**
 * Remote implementation of [CategoryDetailsRepository].
 * Fetches real category details and its lessons from the FastAPI server.
 */
class RemoteCategoryDetailsRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : CategoryDetailsRepository {

    override suspend fun getCategoryDetails(categoryId: String): CategoryDetails {
        // Calls the suspended function in ApiService.
        // The AuthInterceptor automatically adds the user's Firebase token.
        return apiService.getCategoryDetails(categoryId)
    }
}