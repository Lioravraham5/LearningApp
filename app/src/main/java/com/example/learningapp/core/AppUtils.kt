package com.example.learningapp.core

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

/**
 * Converts an ISO 8601 date string (e.g., "2026-03-22T15:25:33")
 * into a relative time span string (e.g., "2 days ago", "Today").
 *
 * BEST PRACTICE: This handles timezone conversions automatically,
 * ensuring the time displayed is relative to the user's local timezone,
 * regardless of the server's timezone.
 */
fun String.toRelativeTimeSpan(): String {
    return try {
        // The format returned by our FastAPI server
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        // Our server stores timestamps in UTC
        format.timeZone = TimeZone.getTimeZone("UTC")

        val date = format.parse(this)

        if (date != null) {
            // Android's built-in utility handles the translation and logic perfectly
            DateUtils.getRelativeTimeSpanString(
                date.time,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS
            ).toString()
        } else {
            this
        }
    } catch (e: Exception) {
        // Fallback: If parsing fails (e.g., empty string), just return the original string
        this
    }
}