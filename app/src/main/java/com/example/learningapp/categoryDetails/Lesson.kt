package com.example.learningapp.categoryDetails

/**
 * Represents a single lesson within a category.
 */
data class Lesson(
    val id: String,
    val title: String,
    val progressPercentage: Float, // Values from 0.0f to 1.0f
    val difficulty: LessonDifficulty
)
