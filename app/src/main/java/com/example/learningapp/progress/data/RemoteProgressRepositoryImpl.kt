package com.example.learningapp.progress.data

import android.util.Log
import com.example.learningapp.network.ApiService
import com.example.learningapp.progress.models.Badge
import com.example.learningapp.progress.models.CategoryAchievement
import com.example.learningapp.progress.models.MarkBadgesSeenRequest
import com.example.learningapp.progress.models.OverviewData
import com.example.learningapp.progress.models.UnseenBadge
import javax.inject.Inject

class RemoteProgressRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ProgressRepository {

    private val TAG = "RemoteProgressRepositoryImpl"

    override suspend fun getOverviewData(): OverviewData {
        try {
            return apiService.getOverviewData()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch overview data", e)
            throw e
        }
    }

    override suspend fun getCategoryAchievements(): List<CategoryAchievement> {
        try {
            return apiService.getCategoryAchievements()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch category achievements", e)
            throw e
        }
    }

    override suspend fun getBadges(): List<Badge> {
        try {
            return apiService.getBadges()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch badges", e)
            throw e
        }
    }

    override suspend fun getUnseenBadges(): List<UnseenBadge> {
        try {
            return apiService.getUnseenBadges()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch unseen badges", e)
            throw e
        }
    }

    override suspend fun markBadgesSeen(badgeIds: List<String>) {
        try {
            apiService.markBadgesSeen(MarkBadgesSeenRequest(badgeIds))
        } catch (e: Exception) {
            Log.e(TAG, "Failed to mark badges as seen: $badgeIds", e)
            throw e
        }
    }
}