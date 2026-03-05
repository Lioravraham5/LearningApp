package com.example.learningapp.progress

/**
 * Represents a badge in the "Badges" tab.
 */
data class Badge(
    val id: String,
    val title: String,
    val description: String,
    val iconRes: Int,
    val isAchieved: Boolean
)
