package com.example.learningapp.progress.data

import com.example.learningapp.progress.models.Badge
import com.example.learningapp.progress.models.CategoryAchievement
import com.example.learningapp.progress.models.OverviewData
import com.example.learningapp.progress.models.UnseenBadge

interface ProgressRepository {
    suspend fun getOverviewData(): OverviewData
    suspend fun getCategoryAchievements(): List<CategoryAchievement>
    suspend fun getBadges(): List<Badge>
    suspend fun getUnseenBadges(): List<UnseenBadge>
    suspend fun markBadgesSeen(badgeIds: List<String>)
}