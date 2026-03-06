package com.example.learningapp.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningapp.auth.AuthRepository
import com.example.learningapp.auth.AuthResult
import com.example.learningapp.auth.generateDisplayName
import com.example.learningapp.avatar.AvatarType
import com.example.learningapp.core.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val userEmail: String = "",
    val selectedAvatar: AvatarType = AvatarType.MALE,
    val error: String? = null,
    // A flag to signal the UI that the user has logged out or deleted their account, so the UI can navigate back to the Login screen.
    val isLoggedOut: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    private val _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    init {
        loadInitialData()
        observeAvatarChanges()
    }

    /**
     * Loads the initial user data (like their name) from the AuthRepository.
     */
    private fun loadInitialData() {
        val user = authRepository.getCurrentUser()
        _profileState.update {
            it.copy(
                userName = user.generateDisplayName(),
                userEmail = user?.email ?: ""
            )
        }
    }

    /**
     * Listens to the DataStore flow.
     * Whenever the avatar changes (even from another screen), the UI state updates automatically.
     */
    private fun observeAvatarChanges() {
        viewModelScope.launch {
            userSettingsRepository.avatarTypeFlow.collect { avatarType ->
                _profileState.update { it.copy(selectedAvatar = avatarType) }
            }
        }
    }

    /**
     * Updates the avatar selection in the DataStore.
     */
    fun updateAvatar(newAvatar: AvatarType) {
        viewModelScope.launch {
            userSettingsRepository.saveAvatarType(newAvatar)
        }
    }

    /**
     * Updates the user's display name in Firebase.
     */
    fun updateDisplayName(newName: String) {
        if (newName.isBlank()) return

        viewModelScope.launch {
            _profileState.update { it.copy(isLoading = true, error = null) }

            // Using the new function we added to AuthRepository
            val result = authRepository.updateDisplayName(newName)

            when (result) {
                is AuthResult.Success -> {
                    // Update the local state to reflect the new name
                    _profileState.update { it.copy(isLoading = false, userName = newName) }
                }
                is AuthResult.Error -> {
                    _profileState.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }

    /**
     * Logs the user out and triggers navigation to the login screen.
     */
    fun logout() {
        authRepository.logout()
        _profileState.update { it.copy(isLoggedOut = true) }
    }

    /**
     * Permanently deletes the user's account and triggers navigation to the login screen.
     */
    fun deleteAccount() {
        viewModelScope.launch {
            _profileState.update { it.copy(isLoading = true, error = null) }

            val result = authRepository.deleteAccount()

            when (result) {
                is AuthResult.Success -> {
                    _profileState.update { it.copy(isLoading = false, isLoggedOut = true) }
                }
                is AuthResult.Error -> {
                    _profileState.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }

    /**
     * Clears the error message from the state (e.g., after the user dismisses an error dialog).
     */
    fun clearError() {
        _profileState.update { it.copy(error = null) }
    }
}