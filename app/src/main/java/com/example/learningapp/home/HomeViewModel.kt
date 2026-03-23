package com.example.learningapp.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningapp.auth.AuthRepository
import com.example.learningapp.auth.generateDisplayName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


// Represents the entire state of the Home screen.
data class HomeState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val categories: List<Category> = emptyList(),
    val error: String? = null
)

// Hilt injects the dependencies into this ViewModel.
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    val TAG = "HomeViewModel"

    // Backing property for the UI state.
    private val _homeState = MutableStateFlow(HomeState())

    // Public state exposed to the UI.
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    init {
        loadHomeData()
    }

    // Fetches user data and categories.
    fun loadHomeData() {
        viewModelScope.launch {
            // Show loading state.
            _homeState.update { it.copy(isLoading = true) }

            try {
                // Fetch categories from the repository.
                val categories = repository.getCategories()
                Log.d(TAG, "Successfully fetched categories: $categories")

                // Fetch the current logged-in user
                val currentUser = authRepository.getCurrentUser()

                // Determine the name to display using a smart fallback mechanism
                val displayName = currentUser.generateDisplayName()

                _homeState.update {
                    it.copy(
                        isLoading = false,
                        userName = displayName,
                        categories = categories
                    )
                }
            } catch (e: Exception) {
                // Update state on error.
                _homeState.update {
                    Log.d(TAG, "Failed to load home data: ${e.message}")

                    it.copy(
                        isLoading = false,
                        error = e.localizedMessage ?: "Failed to load data"
                    )
                }
            }
        }
    }

    /**
     * Refreshes lightweight user data.
     * Called every time the Home screen becomes active to ensure data is synced.
     */
    fun refreshUser() {
        try {
            val currentUser = authRepository.getCurrentUser()
            val displayName = currentUser.generateDisplayName()

            // Update only the user name in the state, keep everything else as is
            _homeState.update {
                it.copy(userName = displayName)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Failed to refresh user data: ${e.message}")
        }
    }
}