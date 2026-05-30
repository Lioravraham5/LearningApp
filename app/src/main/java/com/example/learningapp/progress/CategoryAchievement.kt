package com.example.learningapp.progress

import com.google.gson.annotations.SerializedName

data class CategoryAchievement(
    @SerializedName("category_id") val categoryId: String,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("icon") val iconUrl: String?,
    @SerializedName("average_score") val averageScore: Int,
    @SerializedName("completed_lessons") val completedLessons: Int,
    @SerializedName("in_progress_lessons") val inProgressLessons: Int,
    @SerializedName("un_done_lessons") val unDoneLessons: Int,
    @SerializedName("total_lessons") val totalLessons: Int,
)
