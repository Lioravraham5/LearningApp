package com.example.learningapp.categoryDetails

import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * A mock implementation of the repository to simulate network responses.
 * Later, you can easily swap this out for a "RemoteCategoryDetailsRepositoryImpl" using Retrofit.
 */
class MockCategoryDetailsRepositoryImpl @Inject constructor() : CategoryDetailsRepository {

    override suspend fun getCategoryDetails(categoryId: String): CategoryDetails {
        // Simulate network latency (loading state will be visible in the UI)
        delay(1000)

        // Simulate a database/server lookup based on the requested ID
        return when (categoryId) {
            "1" -> CategoryDetails(
                id = "1",
                title = "Job Interview",
                description = "Master professional language and soft skills for successful job interviews in English.",
                iconRes = android.R.drawable.ic_menu_camera,
                lessons = listOf(
                    Lesson(id = "l1_1", title = "Introduction to Interviews", progressPercentage = 1.0f, difficulty = LessonDifficulty.EASY),
                    Lesson(id = "l1_2", title = "Answering 'Tell me about yourself'", progressPercentage = 0.5f, difficulty = LessonDifficulty.MEDIUM),
                    Lesson(id = "l1_3", title = "Salary Negotiation Tactics", progressPercentage = 0.0f, difficulty = LessonDifficulty.HARD)
                )
            )
            "2" -> CategoryDetails(
                id = "2",
                title = "Trip Abroad",
                description = "Learn essential vocabulary for traveling, booking hotels, and navigating airports.",
                iconRes = android.R.drawable.ic_menu_mapmode,
                lessons = listOf(
                    Lesson(id = "l2_1", title = "At the Airport", progressPercentage = 1.0f, difficulty = LessonDifficulty.EASY),
                    Lesson(id = "l2_2", title = "Booking a Hotel Room", progressPercentage = 0.0f, difficulty = LessonDifficulty.MEDIUM)
                )
            )
            "3" -> CategoryDetails(
                id = "3",
                title = "Everyday Small Talk",
                description = "Build confidence in daily interactions, making new friends, and casual networking.",
                iconRes = android.R.drawable.ic_btn_speak_now,
                lessons = listOf(
                    Lesson(id = "l3_1", title = "Greeting Neighbors", progressPercentage = 1.0f, difficulty = LessonDifficulty.EASY),
                    Lesson(id = "l3_2", title = "Ordering Coffee & Food", progressPercentage = 0.8f, difficulty = LessonDifficulty.EASY),
                    Lesson(id = "l3_3", title = "Keeping a Conversation Going", progressPercentage = 0.2f, difficulty = LessonDifficulty.MEDIUM)
                )
            )
            "4" -> CategoryDetails(
                id = "4",
                title = "Business Emails",
                description = "Learn to write clear, professional, and effective emails for the corporate world.",
                iconRes = android.R.drawable.ic_dialog_email,
                lessons = listOf(
                    Lesson(id = "l4_1", title = "Subject Lines that Work", progressPercentage = 1.0f, difficulty = LessonDifficulty.EASY),
                    Lesson(id = "l4_2", title = "Formal vs. Informal Tone", progressPercentage = 0.0f, difficulty = LessonDifficulty.MEDIUM),
                    Lesson(id = "l4_3", title = "Handling Client Complaints", progressPercentage = 0.0f, difficulty = LessonDifficulty.HARD)
                )
            )
            "5" -> CategoryDetails(
                id = "5",
                title = "At the Restaurant",
                description = "Ordering food and talking to waiters.",
                iconRes = android.R.drawable.ic_menu_view,
                lessons = listOf(
                    Lesson(id = "l5_1", title = "Reserving a Table", progressPercentage = 0.0f, difficulty = LessonDifficulty.EASY),
                    Lesson(id = "l5_2", title = "Understanding the Menu", progressPercentage = 0.0f, difficulty = LessonDifficulty.MEDIUM)
                )
            )
            "6" -> CategoryDetails(
                id = "6",
                title = "Shopping",
                description = "Master phrases for buying clothes and groceries.",
                iconRes = android.R.drawable.ic_menu_manage,
                lessons = listOf(
                    Lesson(id = "l6_1", title = "Asking for Prices", progressPercentage = 0.0f, difficulty = LessonDifficulty.EASY),
                    Lesson(id = "l6_2", title = "Returning an Item", progressPercentage = 0.0f, difficulty = LessonDifficulty.HARD)
                )
            )
            // Simulating a 404 Not Found error from a server
            else -> throw IllegalArgumentException("Category with ID $categoryId not found on the server.")
        }
    }
}