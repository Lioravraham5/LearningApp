package com.example.learningapp.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val repository: HomeRepository
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
    private fun loadHomeData() {
        viewModelScope.launch {
            // Show loading state.
            _homeState.update { it.copy(isLoading = true) }

            try {
                // Fetch categories from the repository.
                val categories = repository.getCategories()
                Log.d(TAG, "Successfully fetched categories: $categories")

                // Update state on success.
                // TODO: "Student Name" is hardcoded; replace with AuthRepository data later.
                _homeState.update {
                    it.copy(
                        isLoading = false,
                        userName = "Student Name",
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
}