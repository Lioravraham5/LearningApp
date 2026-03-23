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

    // Internal mutable state holding the current status of the screen.
    // We start in the Idle state before the fetching begins.
    private val _uiState = MutableStateFlow<UiState<LessonDetails>>(UiState.Idle)

    // Public immutable state exposed to the Compose UI.
    // The UI will collect this safely using collectAsStateWithLifecycle().
    val uiState: StateFlow<UiState<LessonDetails>> = _uiState.asStateFlow()

    init {
        // Retrieve the "lessonId" exactly as it was named in the NavGraph arguments
        // in Step 1: navArgument("lessonId") { type = NavType.StringType }
        val lessonId = savedStateHandle.get<String>("lessonId")

        if (lessonId != null) {
            loadLessonDetails(lessonId)
        } else {
            // Failsafe: If for some reason the navigation was triggered without an ID,
            // we immediately notify the UI to show an error, preventing a crash or blank screen.
            _uiState.value = UiState.Error("Lesson ID is missing.")
            Log.d(TAG, "Initialization failed: Lesson ID is null.")
        }
    }

    /**
     * Fetches the lesson details from the repository and updates the UI state.
     * @param lessonId The unique ID retrieved from the SavedStateHandle.
     */
    private fun loadLessonDetails(lessonId: String) {
        // BEST PRACTICE: Use viewModelScope to launch coroutines.
        // This ensures the network request is automatically canceled if the user
        // navigates away from the screen before the request finishes, preventing memory leaks.
        viewModelScope.launch {
            // 1. Notify the UI that we are starting the network call so it can show a spinner.
            _uiState.value = UiState.Loading

            try {
                // 2. Fetch the data from the repository (suspends until data is ready).
                val details = repository.getLessonDetails(lessonId)

                Log.d(TAG, "Successfully loaded details for lessonId: $lessonId")

                // 3. On success, update the state with the fetched data.
                _uiState.value = UiState.Success(details)

            } catch (e: Exception) {
                Log.e(TAG, "Failed to load details for lessonId: $lessonId", e)

                // 4. On failure, update the state with the error message so the UI can show a fallback.
                _uiState.value = UiState.Error(e.localizedMessage ?: "An unexpected error occurred.")
            }
        }
    }
}