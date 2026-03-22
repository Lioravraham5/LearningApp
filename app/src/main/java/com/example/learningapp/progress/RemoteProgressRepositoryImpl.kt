package com.example.learningapp.progress

import com.example.learningapp.network.ApiService
import javax.inject.Inject

class RemoteProgressRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ProgressRepository {

    override suspend fun getOverviewData(): OverviewData {
        return apiService.getOverviewData()
    }

    override suspend fun getCategoryAchievements(): List<CategoryAchievement> {
        return apiService.getCategoryAchievements()
    }

    override suspend fun getBadges(): List<Badge> {
        return apiService.getBadges()
    }
}