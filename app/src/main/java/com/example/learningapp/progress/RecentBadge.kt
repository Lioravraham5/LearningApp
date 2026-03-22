package com.example.learningapp.progress

import com.google.gson.annotations.SerializedName

/**
 * Represents a badge specifically formatted for the "Recent Achievements" section.
 * It contains only the data needed for this specific UI component, including a pre-formatted date string.
 */
data class RecentBadge(
    val id: String,
    val title: String,
    //@SerializedName("icon") val iconUrl: String?,
    val earnedDate: String // Pre-formatted for the UI (e.g., "2 days ago")
) {
    val iconUrl: Int = android.R.drawable.star_on
}
