package com.example.learningapp.categoryDetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningapp.core.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Category Details Screen.
 * Manages the UI state and handles data fetching.
 */
@HiltViewModel
class CategoryDetailsViewModel @Inject constructor(
    private val repository: CategoryDetailsRepository,
    // BEST PRACTICE: Hilt injects the SavedStateHandle which contains navigation arguments!
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val TAG = "CategoryDetailsViewModel"

    // BEST PRACTICE: Fail-fast validation. Ensures 'categoryId' is non-null
    // and crashes immediately if the required navigation argument is missing.
    // The follow message will present with the error in the logs.
    private val categoryId: String = checkNotNull(savedStateHandle["categoryId"]) {
        "CRITICAL ERROR: 'categoryId' is missing from navigation arguments!"
    }

    // Internal mutable state holding the current status of the screen
    private val _uiState = MutableStateFlow<UiState<CategoryDetails>>(UiState.Idle)

    // Public immutable state exposed to the Compose UI
    val uiState: StateFlow<UiState<CategoryDetails>> = _uiState.asStateFlow()

    init {
        loadCategoryDetails()
    }

    /**
     * Fetches the category details from the repository and updates the UI state accordingly.
     * Made public so the UI can trigger it again (Retry) if a network error occurs.
     */
    fun loadCategoryDetails() {
        viewModelScope.launch {
            // 1. Notify the UI that we are loading data
            _uiState.value = UiState.Loading

            try {
                // 2. Fetch the data using the immutable categoryId
                val details = repository.getCategoryDetails(categoryId)

                Log.d(TAG, "Successfully loaded details for categoryId: $categoryId. CategoryDetails: $details")
                // 3. On success, update the state with the fetched data
                _uiState.value = UiState.Success(details)
            } catch (e: Exception) {
                Log.d(TAG, "Failed to load details for categoryId: $categoryId, Error: ${e.message}")
                // 4. On failure, update the state with the error message
                _uiState.value = UiState.Error(e.localizedMessage ?: "An unexpected error occurred.")
            }
        }
    }
}
