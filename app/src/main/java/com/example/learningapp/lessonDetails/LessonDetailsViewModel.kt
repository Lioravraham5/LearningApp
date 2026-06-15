package com.example.learningapp.lessonDetails

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
 * ViewModel for the Lesson Details Screen.
 * Manages the UI state and handles fetching the specific lesson's data.
 */
@HiltViewModel
class LessonDetailsViewModel @Inject constructor(
    private val repository: LessonDetailsRepository,
    // BEST PRACTICE: Hilt automatically injects the SavedStateHandle.
    // This allows the ViewModel to access navigation arguments independently,
    // keeping the UI completely decoupled from data-fetching logic.
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val TAG = "LessonDetailsViewModel"

    // BEST PRACTICE: Fail-fast validation. Ensures 'lessonId' is non-null
    // and crashes immediately if the required navigation argument is missing.
    // The follow message will present with the error in the logs.
    private val lessonId: String = checkNotNull(savedStateHandle["lessonId"]) {
        "CRITICAL ERROR: lessonId is missing from navigation arguments!"
    }

    // Internal mutable state holding the current status of the screen.
    // We start in the Idle state before the fetching begins.
    private val _uiState = MutableStateFlow<UiState<LessonDetails>>(UiState.Idle)

    // Public immutable state exposed to the Compose UI.
    // The UI will collect this safely using collectAsStateWithLifecycle().
    val uiState: StateFlow<UiState<LessonDetails>> = _uiState.asStateFlow()

    init {
        // Initial load
        loadLessonDetails()
    }

    /**
     * Fetches the lesson details.
     * @param isRefresh If true, avoids emitting UiState.Loading to prevent UI flickering when returning to the screen.
     */
    fun loadLessonDetails(isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (!isRefresh) {
                _uiState.value = UiState.Loading
            }

            try {
                val details = repository.getLessonDetails(lessonId)
                Log.d(TAG, "Successfully loaded details for lessonId: $lessonId")
                // This will smoothly replace the old Success state with the new Success state
                _uiState.value = UiState.Success(details)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load details for lessonId: $lessonId", e)
                // Only show error screen if we don't already have data (optional, but good practice)
                if (_uiState.value !is UiState.Success) {
                    _uiState.value = UiState.Error(e.localizedMessage ?: "An unexpected error occurred.")
                }
            }
        }
    }
}