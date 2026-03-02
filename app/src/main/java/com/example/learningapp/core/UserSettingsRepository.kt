package com.example.learningapp.core

import kotlinx.coroutines.flow.Flow

/**
 * Interface for managing global user preferences.
 * Acts as the Single Source of Truth for app-wide settings.
 */
interface UserSettingsRepository {

    // Exposes the selected avatar as a reactive Flow.
    val avatarTypeFlow: Flow<AvatarType>

    // Updates the selected avatar in the local storage.
    suspend fun saveAvatarType(avatarType: AvatarType)
}