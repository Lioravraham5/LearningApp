package com.example.learningapp.progress.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningapp.core.BadgeCelebrationCoordinator
import com.example.learningapp.core.UiState
import com.example.learningapp.progress.data.ProgressRepository
import com.example.learningapp.progress.models.Badge
import com.example.learningapp.progress.models.CategoryAchievement
import com.example.learningapp.progress.models.OverviewData
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
    private val repository: ProgressRepository,
    private val badgeCelebrationCoordinator: BadgeCelebrationCoordinator
) : ViewModel() {

    private val TAG = "ProgressViewModel"

    // Backing property for the UI state
    private val _progressState = MutableStateFlow(ProgressState())
    // Public state exposed to the UI
    val progressState: StateFlow<ProgressState> = _progressState.asStateFlow()

    init {
        // Immediately load the first tab's data when the screen is created
        loadOverview()

        // Badges earned elsewhere in the app (lesson completion, or the resume-time fallback
        // check) don't otherwise invalidate this ViewModel's cache - bottom-nav navigation
        // preserves this instance (popUpTo saveState/restoreState), so a badge earned after the
        // Overview/Badges tab was already visited this session would otherwise show stale data
        // indefinitely.
        viewModelScope.launch {
            badgeCelebrationCoordinator.badgesChanged.collect {
                Log.d(TAG, "Badge change detected, force-refreshing overview and badges")
                refreshBadgeRelatedState()
            }
        }
    }

    /**
     * Re-fetches Overview (badge count + recent achievements) and Badges, bypassing the normal
     * lazy-load guard. Must actively re-fetch, not just reset to Idle: if the user is already on
     * the Badges tab when this fires, resetting to Idle alone wouldn't retrigger anything, since
     * the pager's LaunchedEffect only re-invokes onTabSelected on a page *change*.
     */
    private fun refreshBadgeRelatedState() {
        loadOverview(forceRefresh = true)
        loadBadges(forceRefresh = true)
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

    private fun loadOverview(forceRefresh: Boolean = false) {
        // Lazy Loading check: Only fetch if we haven't started yet, or if there was a previous error
        val currentState = _progressState.value.overviewState
        if (!forceRefresh && (currentState is UiState.Success || currentState is UiState.Loading)) return

        viewModelScope.launch {
            _progressState.update { it.copy(overviewState = UiState.Loading) }
            try {
                val data = repository.getOverviewData()
                Log.d(TAG, "Successfully loaded overview data")
                _progressState.update { it.copy(overviewState = UiState.Success(data)) }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load overview data", e)
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
                Log.d(TAG, "Successfully loaded category achievements")
                _progressState.update { it.copy(achievementsState = UiState.Success(data)) }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load category achievements", e)
                _progressState.update {
                    it.copy(achievementsState = UiState.Error(e.localizedMessage ?: "Unknown error occurred"))
                }
            }
        }
    }

    private fun loadBadges(forceRefresh: Boolean = false) {
        // Lazy Loading check
        val currentState = _progressState.value.badgesState
        if (!forceRefresh && (currentState is UiState.Success || currentState is UiState.Loading)) return

        viewModelScope.launch {
            _progressState.update { it.copy(badgesState = UiState.Loading) }
            try {
                val data = repository.getBadges()
                Log.d(TAG, "Successfully loaded badges")
                _progressState.update { it.copy(badgesState = UiState.Success(data)) }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load badges", e)
                _progressState.update {
                    it.copy(badgesState = UiState.Error(e.localizedMessage ?: "Unknown error occurred"))
                }
            }
        }
    }
}