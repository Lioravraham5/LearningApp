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
                iconRes = android.R.drawable.ic_menu_camera,
                totalLessons = 3, // Matches the 3 lessons in details
                completedLessons = 1 // 1 lesson is at 100%
            ),
            Category(
                id = "2",
                title = "Trip Abroad",
                description = "Learn essential vocabulary for traveling, booking hotels, and navigating airports.",
                iconRes = android.R.drawable.ic_menu_mapmode,
                totalLessons = 2,
                completedLessons = 1
            ),
            Category(
                id = "3",
                title = "Everyday Small Talk",
                description = "Build confidence in daily interactions, making new friends, and casual networking.",
                iconRes = android.R.drawable.ic_btn_speak_now,
                totalLessons = 3,
                completedLessons = 1
            ),
            Category(
                id = "4",
                title = "Business Emails",
                description = "Learn to write clear, professional, and effective emails for the corporate world.",
                iconRes = android.R.drawable.ic_dialog_email,
                totalLessons = 3,
                completedLessons = 1
            ),
            Category(
                id = "5",
                title = "At the Restaurant",
                description = "Ordering food and talking to waiters.",
                iconRes = android.R.drawable.ic_menu_view,
                totalLessons = 2,
                completedLessons = 0
            ),
            Category(
                id = "6",
                title = "Shopping",
                description = "Master phrases for buying clothes and groceries.",
                iconRes = android.R.drawable.ic_menu_manage,
                totalLessons = 2,
                completedLessons = 0
            )
        )
    }
}