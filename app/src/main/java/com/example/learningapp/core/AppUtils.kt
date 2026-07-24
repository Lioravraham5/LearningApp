package com.example.learningapp.core

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

/**
 * Converts an ISO 8601 date string (e.g., "2026-03-22T15:25:33")
 * into a relative time span string (e.g., "2 days ago", "Yesterday").
 *
 * BEST PRACTICE: This handles timezone conversions automatically,
 * ensuring the time displayed is relative to the user's local timezone,
 * regardless of the server's timezone.
 *
 * Deliberately hand-rolled in English rather than using Android's
 * DateUtils.getRelativeTimeSpanString() - that API always formats using the device's system
 * locale (it reads from the global Resources.getSystem(), not this app's own resources, so it
 * can't be pinned per-call), which produced e.g. Hebrew text on a Hebrew-locale device even
 * though the rest of this app's UI is hardcoded English with no localization system.
 */
fun String.toRelativeTimeSpan(): String {
    return try {
        // The format returned by our FastAPI server
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        // Our server stores timestamps in UTC
        format.timeZone = TimeZone.getTimeZone("UTC")

        val date = format.parse(this) ?: return this

        val diffMillis = System.currentTimeMillis() - date.time
        val minutes = diffMillis / (60 * 1000)
        val hours = diffMillis / (60 * 60 * 1000)
        val days = diffMillis / (24 * 60 * 60 * 1000)

        when {
            minutes < 1 -> "Just now"
            minutes < 60 -> "$minutes minute${if (minutes == 1L) "" else "s"} ago"
            hours < 24 -> "$hours hour${if (hours == 1L) "" else "s"} ago"
            days == 1L -> "Yesterday"
            days < 7 -> "$days days ago"
            else -> {
                val weeks = days / 7
                "$weeks week${if (weeks == 1L) "" else "s"} ago"
            }
        }
    } catch (e: Exception) {
        // Fallback: If parsing fails (e.g., empty string), just return the original string
        this
    }
}