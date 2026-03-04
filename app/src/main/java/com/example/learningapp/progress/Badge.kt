package com.example.learningapp.progress

data class Badge(
    val id: String,
    val title: String,
    val description: String,
    val iconRes: Int,
    val isAchieved: Boolean
)
