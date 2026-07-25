package com.example.learningapp.progress.models

import com.google.gson.annotations.SerializedName

/**
 * A badge the user has earned but hasn't yet been shown a celebration for.
 * Maps to the backend's `UnseenBadgeResponse` (GET /progress/badges/unseen).
 */
data class UnseenBadge(
    val id: String,
    val title: String,
    val description: String,
    @SerializedName("achieved_at") val achievedAt: String
)
