package com.example.learningapp.lessonEnd

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningapp.core.UiState
import com.example.learningapp.lessonEnd.models.LessonCompleteResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for the end lesson screen.
 * It automatically triggers the server-side aggregation of the lesson's score
 * and holds the resulting feedback state for the UI to display.
 */
@HiltViewModel
class LessonEndViewModel @Inject constructor(
    private val repository: LessonEndRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val TAG = "LessonEndViewModel"

    // We enforce that the navigation graph MUST provide both lessonId and runId.
    // If they are missing, it's a developer error and the app will crash immediately with a clear message.
    private val lessonId: String = checkNotNull(savedStateHandle["lessonId"]) {
        "CRITICAL ERROR: 'lessonId' is missing from navigation arguments!"
    }

    val runId: String = checkNotNull(savedStateHandle["runId"]) {
        "CRITICAL ERROR: 'runId' is missing from navigation arguments!"
    }

    // The internal mutable state representing the screen's current status
    private val _uiState = MutableStateFlow<UiState<LessonCompleteResponse>>(UiState.Idle)

    // The public immutable state exposed to the Compose UI layer
    val uiState: StateFlow<UiState<LessonCompleteResponse>> = _uiState.asStateFlow()

    init {
        // Trigger the finalization process as soon as the ViewModel is created
        finalizeLesson()
    }

    /**
     * Communicates with the repository to officially end the lesson session
     * and retrieve the calculated score and feedback.
     */
    fun finalizeLesson() {
        viewModelScope.launch {
            // 1. Notify the UI to show the loading state (e.g., a spinner)
            _uiState.value = UiState.Loading

            // 2. Call the suspend function in the repository
            val result = repository.completeLesson(lessonId = lessonId, runId = runId)

            // 3. Handle the Result object gracefully
            result.onSuccess { response ->
                Log.d(TAG, "Lesson finalized successfully. Score: ${response.averageScore}")
                _uiState.value = UiState.Success(response)
            }.onFailure { error ->
                Log.e(TAG, "Failed to finalize lesson: ${error.localizedMessage}", error)
                _uiState.value = UiState.Error(error.localizedMessage ?: "An unexpected error occurred.")
            }
        }
    }
}