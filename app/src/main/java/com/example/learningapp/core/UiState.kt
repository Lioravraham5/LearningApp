package com.example.learningapp.core

/**
 * A generic sealed class representing the different states of a data fetch operation.
 * This is crucial for implementing Lazy Loading in the UI.
 */
sealed class UiState<out T> {
    /** The initial state before any data is requested. */
    object Idle : UiState<Nothing>()

    /** Data is currently being fetched from the repository. */
    object Loading : UiState<Nothing>()

    /** Data was successfully fetched. */
    data class Success<out T>(val data: T) : UiState<T>()

    /** An error occurred during the fetch operation. */
    data class Error(val message: String) : UiState<Nothing>()
}