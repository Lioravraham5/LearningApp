package com.example.learningapp.progress

import com.google.gson.annotations.SerializedName

data class OverviewData(
    @SerializedName("average_score") val averageScore: Int,
    @SerializedName("total_completed_lessons") val totalCompletedLessons: Int,
    @SerializedName("total_earned_badges") val totalEarnedBadges: Int,
    @SerializedName("daily_streak") val dailyStreak: Int,
    @SerializedName("recent_achievements") val recentAchievements: List<RecentBadge>
)
