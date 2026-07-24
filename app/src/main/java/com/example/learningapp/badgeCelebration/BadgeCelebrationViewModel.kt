package com.example.learningapp.badgeCelebration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningapp.core.BadgeCelebrationCoordinator
import com.example.learningapp.progress.Badge
import com.example.learningapp.progress.ProgressRepository
import com.example.learningapp.progress.UnseenBadge
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Drives the global badge-celebration overlay (see BadgeCelebrationHost). Best-effort by design -
 * fetch failures are swallowed since this is a background reconciliation, never surfaced as an
 * error to the user.
 */
@HiltViewModel
class BadgeCelebrationViewModel @Inject constructor(
    private val repository: ProgressRepository,
    private val coordinator: BadgeCelebrationCoordinator
) : ViewModel() {

    private val TAG = "BadgeCelebrationViewModel"

    val currentCelebration: StateFlow<Badge?> = coordinator.pendingBadges
        .map { it.firstOrNull() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun checkForUnseenBadges() {
        viewModelScope.launch {
            runCatching { repository.getUnseenBadges() }
                .onSuccess { unseen ->
                    Log.d(TAG, "Fetched unseen badges: ${unseen.map { it.id }}")
                    coordinator.enqueue(unseen.map { it.toBadge() })
                }
                .onFailure { e -> Log.e(TAG, "Failed to fetch unseen badges", e) }
        }
    }

    fun dismissCurrentCelebration() {
        Log.d(TAG, "Dismissing current celebration")
        viewModelScope.launch { coordinator.dismissCurrent() }
    }

    fun clearPendingCelebrations() {
        coordinator.clear()
    }

    private fun UnseenBadge.toBadge() = Badge(
        id = id,
        title = title,
        description = description,
        isAchieved = true
    )
}
