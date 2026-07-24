package com.example.learningapp.progress

import com.google.gson.annotations.SerializedName

/**
 * Payload for POST /progress/badges/seen - acknowledges the celebration for these badges
 * has been shown to the user.
 */
data class MarkBadgesSeenRequest(
    @SerializedName("badge_ids") val badgeIds: List<String>
)
