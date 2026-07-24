package com.example.learningapp.progress

import com.google.gson.annotations.SerializedName

/**
 * Represents a badge specifically formatted for the "Recent Achievements" section.
 * It contains only the data needed for this specific UI component, including a pre-formatted date string.
 */
data class RecentBadge(
    val id: String,
    val title: String,
    // Backend field is "earned_date" (snake_case) - without this mapping Gson silently leaves
    // this "non-null" String field as null on any object built from a real network response
    // (Gson matches JSON keys by exact name, doesn't do snake_case->camelCase conversion), which
    // then crashes toRelativeTimeSpan()'s compiler-inserted non-null receiver check at runtime.
    @SerializedName("earned_date") val earnedDate: String // Pre-formatted for the UI (e.g., "2 days ago")
)
