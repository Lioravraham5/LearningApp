package com.example.learningapp.core

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.learningapp.avatar.AvatarType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of UserSettingsRepository using Jetpack DataStore.
 */
class UserSettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserSettingsRepository {

    private companion object {
        // Key used to store and retrieve the avatar preference
        val AVATAR_TYPE_KEY = stringPreferencesKey("avatar_type")
    }

    override val avatarTypeFlow: Flow<AvatarType> = dataStore.data
        .map { preferences ->
            // Read the string from DataStore. Default to MALE if empty.
            val savedValue = preferences[AVATAR_TYPE_KEY] ?: AvatarType.MALE.name

            // Safely convert the string back to the Enum
            try {
                AvatarType.valueOf(savedValue)
            } catch (e: IllegalArgumentException) {
                AvatarType.MALE
            }
        }

    override suspend fun saveAvatarType(avatarType: AvatarType) {
        dataStore.edit { preferences ->
            // Save the Enum as a String
            preferences[AVATAR_TYPE_KEY] = avatarType.name
        }
    }
}