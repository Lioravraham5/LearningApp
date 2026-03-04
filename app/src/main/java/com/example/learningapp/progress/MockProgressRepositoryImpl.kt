package com.example.learningapp.progress

import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * A mock implementation of [ProgressRepository] for UI development and testing.
 * Simulates network delays and returns hardcoded realistic data.
 */
class MockProgressRepositoryImpl @Inject constructor() : ProgressRepository {

    override suspend fun getOverviewData(): OverviewData {
        delay(1000) // Simulate network request
        return OverviewData(
            averageScore = 85,
            totalCompletedLessons = 24,
            totalEarnedBadges = 6,
            dailyStreak = 12
        )
    }

    override suspend fun getCategoryAchievements(): List<CategoryAchievement> {
        delay(1500) // Simulate slightly longer network request
        return listOf(
            CategoryAchievement(
                categoryId = "1",
                categoryName = "Trip Abroad",
                iconRes = android.R.drawable.ic_menu_camera,
                averageScore = 92,
                completedLessons = 8,
                inProgressLessons = 2
            ),
            CategoryAchievement(
                categoryId = "2",
                categoryName = "Job Interview",
                iconRes = android.R.drawable.ic_menu_gallery,
                averageScore = 78,
                completedLessons = 5,
                inProgressLessons = 4
            ),
            CategoryAchievement(
                categoryId = "3",
                categoryName = "Daily Life",
                iconRes = android.R.drawable.ic_menu_manage,
                averageScore = 100,
                completedLessons = 10,
                inProgressLessons = 0
            )
        )
    }

    override suspend fun getBadges(): List<Badge> {
        delay(1200) // Simulate network request
        return listOf(
            Badge(
                id = "b1",
                title = "First Step",
                description = "Completed your very first lesson.",
                iconRes = android.R.drawable.star_on, // Placeholder icon
                isAchieved = true
            ),
            Badge(
                id = "b2",
                title = "10 Days Streak",
                description = "Learned for 10 consecutive days.",
                iconRes = android.R.drawable.star_on,
                isAchieved = true
            ),
            Badge(
                id = "b3",
                title = "Perfect Score",
                description = "Completed a lesson with 100% accuracy.",
                iconRes = android.R.drawable.star_off, // Placeholder for locked
                isAchieved = false
            ),
            Badge(
                id = "b4",
                title = "Globetrotter",
                description = "Completed the 'Trip Abroad' category.",
                iconRes = android.R.drawable.star_off,
                isAchieved = false
            )
        )
    }
}