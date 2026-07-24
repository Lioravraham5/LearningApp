package com.example.learningapp.progress

import com.google.gson.annotations.SerializedName

/**
 * Represents a badge in the "Badges" tab.
 */
data class Badge(
    val id: String,
    val title: String,
    val description: String,
    @SerializedName("is_achieved") val isAchieved: Boolean
)
