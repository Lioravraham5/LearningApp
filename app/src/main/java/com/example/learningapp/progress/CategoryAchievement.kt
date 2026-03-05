package com.example.learningapp.progress

data class CategoryAchievement(
    val categoryId: String,
    val categoryName: String,
    val iconRes: Int,
    val averageScore: Int,
    val completedLessons: Int,
    val inProgressLessons: Int,
    val unDoneLessons: Int,
    val totalLessons: Int,
)
