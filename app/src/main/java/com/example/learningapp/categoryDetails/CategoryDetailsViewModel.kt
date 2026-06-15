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

@HiltViewModel
class CategoryDetailsViewModel @Inject constructor(
    private val repository: CategoryDetailsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val TAG = "CategoryDetailsViewModel"

    private val categoryId: String = checkNotNull(savedStateHandle["categoryId"]) {
        "CRITICAL ERROR: 'categoryId' is missing from navigation arguments!"
    }

    private val _uiState = MutableStateFlow<UiState<CategoryDetails>>(UiState.Idle)
    val uiState: StateFlow<UiState<CategoryDetails>> = _uiState.asStateFlow()

    init {
        loadCategoryDetails()
    }

    /**
     * Fetches the category details.
     * @param isRefresh If true, avoids emitting UiState.Loading to prevent UI flickering.
     */
    fun loadCategoryDetails(isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (!isRefresh) {
                _uiState.value = UiState.Loading
            }

            try {
                val details = repository.getCategoryDetails(categoryId)
                Log.d(TAG, "Successfully loaded details for categoryId: $categoryId")
                _uiState.value = UiState.Success(details)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load details for categoryId: $categoryId", e)
                if (_uiState.value !is UiState.Success) {
                    _uiState.value = UiState.Error(e.localizedMessage ?: "An unexpected error occurred.")
                }
            }
        }
    }
}
