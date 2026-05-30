package com.example.learningapp.home

import kotlinx.coroutines.delay
import javax.inject.Inject

class MockHomeRepositoryImpl @Inject constructor() : HomeRepository {

    override suspend fun getCategories(): List<Category> {

        // for realistic behavior
        delay(1000)

        return listOf(
            Category(
                id = "1",
                title = "Job Interview",
                description = "Master professional language and soft skills for successful job interviews in English.",
                iconUrl = null, // Added missing parameter
                totalLessons = 3,
                completedLessons = 1,
                progressPercentage = 0.33f // 1 out of 3
            ),
            Category(
                id = "2",
                title = "Trip Abroad",
                description = "Learn essential vocabulary for traveling, booking hotels, and navigating airports.",
                iconUrl = null, // Added missing parameter
                totalLessons = 2,
                completedLessons = 1,
                progressPercentage = 0.5f // 1 out of 2
            ),
            Category(
                id = "3",
                title = "Everyday Small Talk",
                description = "Build confidence in daily interactions, making new friends, and casual networking.",
                iconUrl = null, // Added missing parameter
                totalLessons = 3,
                completedLessons = 1,
                progressPercentage = 0.33f
            ),
            Category(
                id = "4",
                title = "Business Emails",
                description = "Learn to write clear, professional, and effective emails for the corporate world.",
                iconUrl = null, // Added missing parameter
                totalLessons = 3,
                completedLessons = 1,
                progressPercentage = 0.33f
            ),
            Category(
                id = "5",
                title = "At the Restaurant",
                description = "Ordering food and talking to waiters.",
                iconUrl = null, // Added missing parameter
                totalLessons = 2,
                completedLessons = 0,
                progressPercentage = 0f
            ),
            Category(
                id = "6",
                title = "Shopping",
                description = "Master phrases for buying clothes and groceries.",
                iconUrl = null, // Added missing parameter
                totalLessons = 2,
                completedLessons = 0,
                progressPercentage = 0f
            )
        )
    }
}