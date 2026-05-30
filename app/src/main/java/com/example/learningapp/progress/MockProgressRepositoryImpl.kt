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
            dailyStreak = 12,
            recentAchievements = listOf(
                RecentBadge(
                    id = "rb1",
                    title = "7 Days Streak",
                    earnedDate = "Today"
                ),
                RecentBadge(
                    id = "rb2",
                    title = "First Step",
                    earnedDate = "2 days ago"
                ),
                RecentBadge(
                    id = "rb3",
                    title = "Perfect Score",
                    earnedDate = "1 week ago"
                )
            )
        )
    }

    override suspend fun getCategoryAchievements(): List<CategoryAchievement> {
        delay(1500) // Simulate slightly longer network request
        return listOf(
            CategoryAchievement(
                categoryId = "1",
                categoryName = "Trip Abroad",
                iconUrl = null,
                averageScore = 92,
                completedLessons = 8,
                inProgressLessons = 2,
                unDoneLessons = 5,
                totalLessons = 15 // 8 + 2 + 5
            ),
            CategoryAchievement(
                categoryId = "2",
                categoryName = "Job Interview",
                iconUrl = null,
                averageScore = 78,
                completedLessons = 5,
                inProgressLessons = 4,
                unDoneLessons = 11,
                totalLessons = 20 // 5 + 4 + 11
            ),
            CategoryAchievement(
                categoryId = "3",
                categoryName = "Daily Life",
                iconUrl = null,
                averageScore = 100,
                completedLessons = 10,
                inProgressLessons = 0,
                unDoneLessons = 0, // 100% Completed!
                totalLessons = 10 // 10 + 0 + 0
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
                isAchieved = true
            ),
            Badge(
                id = "b2",
                title = "10 Days Streak",
                description = "Learned for 10 consecutive days.",
                isAchieved = true
            ),
            Badge(
                id = "b3",
                title = "Perfect Score",
                description = "Completed a lesson with 100% accuracy.",
                isAchieved = false
            ),
            Badge(
                id = "b4",
                title = "Globetrotter",
                description = "Completed the 'Trip Abroad' category.",
                isAchieved = false
            )
        )
    }
}
