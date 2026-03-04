package com.example.learningapp.progress

interface ProgressRepository {
    suspend fun getOverviewData(): OverviewData
    suspend fun getCategoryAchievements(): List<CategoryAchievement>
    suspend fun getBadges(): List<Badge>
}