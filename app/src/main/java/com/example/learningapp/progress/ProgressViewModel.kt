package com.example.learningapp.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningapp.core.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Represents the entire state of the Progress Screen.
 * Each tab holds its own independent [UiState], allowing them to load separately.
 */
data class ProgressState(
    // Overview is the default tab, but starts as Idle until the ViewModel's init block fires
    val overviewState: UiState<OverviewData> = UiState.Idle,

    // These will remain Idle until the user explicitly swipes to their respective tabs
    val achievementsState: UiState<List<CategoryAchievement>> = UiState.Idle,
    val badgesState: UiState<List<Badge>> = UiState.Idle
)

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val repository: ProgressRepository
) : ViewModel() {

    // Backing property for the UI state
    private val _progressState = MutableStateFlow(ProgressState())
    // Public state exposed to the UI
    val progressState: StateFlow<ProgressState> = _progressState.asStateFlow()

    init {
        // Immediately load the first tab's data when the screen is created
        loadOverview()
    }

    /**
     * Triggered by the UI (e.g., LaunchedEffect observing the Pager's current page).
     * @param index 0 = Overview, 1 = Achievements, 2 = Badges
     */
    fun onTabSelected(index: Int) {
        when (index) {
            0 -> loadOverview()
            1 -> loadAchievements()
            2 -> loadBadges()
        }
    }

    private fun loadOverview() {
        // Lazy Loading check: Only fetch if we haven't started yet, or if there was a previous error
        val currentState = _progressState.value.overviewState
        if (currentState is UiState.Success || currentState is UiState.Loading) return

        viewModelScope.launch {
            _progressState.update { it.copy(overviewState = UiState.Loading) }
            try {
                val data = repository.getOverviewData()
                _progressState.update { it.copy(overviewState = UiState.Success(data)) }
            } catch (e: Exception) {
                _progressState.update {
                    it.copy(overviewState = UiState.Error(e.localizedMessage ?: "Unknown error occurred"))
                }
            }
        }
    }

    private fun loadAchievements() {
        // Lazy Loading check
        val currentState = _progressState.value.achievementsState
        if (currentState is UiState.Success || currentState is UiState.Loading) return

        viewModelScope.launch {
            _progressState.update { it.copy(achievementsState = UiState.Loading) }
            try {
                val data = repository.getCategoryAchievements()
                _progressState.update { it.copy(achievementsState = UiState.Success(data)) }
            } catch (e: Exception) {
                _progressState.update {
                    it.copy(achievementsState = UiState.Error(e.localizedMessage ?: "Unknown error occurred"))
                }
            }
        }
    }

    private fun loadBadges() {
        // Lazy Loading check
        val currentState = _progressState.value.badgesState
        if (currentState is UiState.Success || currentState is UiState.Loading) return

        viewModelScope.launch {
            _progressState.update { it.copy(badgesState = UiState.Loading) }
            try {
                val data = repository.getBadges()
                _progressState.update { it.copy(badgesState = UiState.Success(data)) }
            } catch (e: Exception) {
                _progressState.update {
                    it.copy(badgesState = UiState.Error(e.localizedMessage ?: "Unknown error occurred"))
                }
            }
        }
    }
}