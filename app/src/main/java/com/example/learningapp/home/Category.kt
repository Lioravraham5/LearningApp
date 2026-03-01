package com.example.learningapp.home

data class Category(
    val id: String,
    val title: String,
    val description: String,
    val iconRes: Int,
    val totalLessons: Int,
    val completedLessons: Int
) {
    // property for calculate the progress percentage
    val progressPercentage: Float
        get() = if (totalLessons > 0) completedLessons.toFloat() / totalLessons.toFloat() else 0f
}
