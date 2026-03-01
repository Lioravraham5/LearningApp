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
                title = "Trip Abroad",
                description = "Learn essential phrases for traveling and exploring new places",
                iconRes = android.R.drawable.ic_menu_camera,
                totalLessons = 8,
                completedLessons = 2
            ),
            Category(
                id = "2",
                title = "Job Interview",
                description = "Master professional language for successful job interviews",
                iconRes = android.R.drawable.ic_menu_gallery,
                totalLessons = 12,
                completedLessons = 5
            ),
            Category(
                id = "3",
                title = "Daily Life",
                description = "Practice everyday conversations and common situations",
                iconRes = android.R.drawable.ic_menu_manage,
                totalLessons = 5,
                completedLessons = 5
            ),
            Category(
                id = "4",
                title = "Socializing",
                description = "Learn how to introduce yourself and make friends.",
                iconRes = android.R.drawable.ic_menu_myplaces,
                totalLessons = 10,
                completedLessons = 7
            ),
            Category(
                id = "5",
                title = "At the Restaurant",
                description = "Ordering food and talking to waiters.",
                iconRes = android.R.drawable.ic_menu_view,
                totalLessons = 6,
                completedLessons = 0
            ),
            Category(
                id = "6",
                title = "Shopping",
                description = "Master phrases for buying clothes and groceries.",
                iconRes = android.R.drawable.ic_menu_manage,
                totalLessons = 15,
                completedLessons = 3
            )
        )
    }
}