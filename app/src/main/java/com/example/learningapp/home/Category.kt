package com.example.learningapp.home

import com.google.gson.annotations.SerializedName

data class Category(
    val id: String,
    val title: String,
    val description: String,
    @SerializedName("icon") val iconUrl: String?,
    @SerializedName("total_lessons") val totalLessons: Int,
    @SerializedName("completed_lessons") val completedLessons: Int,
    @SerializedName("progress_percentage") val progressPercentage: Float
)
