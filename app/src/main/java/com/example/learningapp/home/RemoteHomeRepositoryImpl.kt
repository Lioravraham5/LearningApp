package com.example.learningapp.home

import com.example.learningapp.network.ApiService
import javax.inject.Inject

/**
 * Remote implementation of [HomeRepository].
 * Fetches real data from the FastAPI server using Retrofit.
 */
class RemoteHomeRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : HomeRepository {

    override suspend fun getCategories(): List<Category> {
        // Calls the suspended function in ApiService.
        // Retrofit automatically handles the background thread and JSON parsing.
        // The AuthInterceptor we built automatically attaches the Firebase Token!
        return apiService.getCategories()
    }
}