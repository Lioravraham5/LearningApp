package com.example.learningapp.categoryDetails

/**
 * An abstraction for the data layer.
 * The ViewModel will interact with this interface, making it completely unaware
 * of whether the data comes from a local Mock, a Room database, or a Retrofit API.
 */
interface CategoryDetailsRepository {

    /**
     * Fetches the complete details and lessons for a specific category.
     * * @param categoryId The unique identifier of the category to fetch.
     * @return [CategoryDetails] containing the header info and the list of lessons.
     * @throws Exception if the category is not found or a network error occurs.
     */
    suspend fun getCategoryDetails(categoryId: String): CategoryDetails
}