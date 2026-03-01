package com.example.learningapp.auth

import com.google.firebase.auth.FirebaseUser

/**
 * Extracts a displayable name from a FirebaseUser.
 * Prioritizes displayName, falls back to email prefix, and finally a default name.
 * * @param defaultName The fallback name if no user details are available.
 * @return A formatted string ready for the UI.
 */
fun FirebaseUser?.generateDisplayName(defaultName: String = "Student"): String {
    // If the user object itself is null, return the default immediately
    this ?: return defaultName
    return displayName?.takeIf { it.isNotBlank() }
        ?: email?.substringBefore("@")
        ?: defaultName
}