package com.example.learningapp.progress

data class OverviewData(
    val averageScore: Int,
    val totalCompletedLessons: Int,
    val totalEarnedBadges: Int,
    val dailyStreak: Int,
    val recentAchievements: List<RecentBadge>
)
