package com.example.learningapp.core

import android.util.Log
import com.example.learningapp.progress.models.Badge
import com.example.learningapp.progress.data.ProgressRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single source of truth for "badges the user hasn't been shown a celebration for yet."
 *
 * Fed by two producers - LessonEndViewModel (the fast path, from a /complete response's
 * `new_badges`) and BadgeCelebrationViewModel (the fallback path, from GET /progress/badges/unseen
 * on app resume) - and consumed by two subscribers - the global celebration overlay
 * (BadgeCelebrationHost) and ProgressViewModel (to invalidate its stale Badges/Overview cache).
 *
 * A durable queue (StateFlow<List<Badge>>) is used instead of a one-shot SharedFlow<Badge> event
 * deliberately: a one-shot event fired before a collector is attached is silently lost, which is
 * exactly the failure mode this feature exists to avoid.
 */
@Singleton
class BadgeCelebrationCoordinator @Inject constructor(
    private val repository: ProgressRepository
) {
    private val TAG = "BadgeCelebrationCoordinator"

    private val _pendingBadges = MutableStateFlow<List<Badge>>(emptyList())
    val pendingBadges: StateFlow<List<Badge>> = _pendingBadges.asStateFlow()

    private val _badgesChanged = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val badgesChanged: SharedFlow<Unit> = _badgesChanged

    /**
     * Enqueues any of [newBadges] not already pending. Emits [badgesChanged] if anything was
     * actually added, so subscribers like ProgressViewModel can refresh their cached state.
     */
    fun enqueue(newBadges: List<Badge>) {
        if (newBadges.isEmpty()) return

        var addedIds: List<String> = emptyList()
        _pendingBadges.update { current ->
            val currentIds = current.map { it.id }.toSet()
            val toAdd = newBadges.filter { it.id !in currentIds }
            addedIds = toAdd.map { it.id }
            current + toAdd
        }
        if (addedIds.isNotEmpty()) {
            Log.d(TAG, "Enqueued new badges: $addedIds")
            _badgesChanged.tryEmit(Unit)
        }
    }

    /**
     * Pops the front of the queue and acks it as seen server-side. Acking happens on dismissal,
     * not on enqueue, so a badge whose dialog never got dismissed (app killed mid-celebration) is
     * correctly re-offered by the next GET /progress/badges/unseen fetch.
     */
    suspend fun dismissCurrent() {
        val dismissed = _pendingBadges.value.firstOrNull() ?: return
        _pendingBadges.update { it.drop(1) }
        runCatching { repository.markBadgesSeen(listOf(dismissed.id)) }
            .onSuccess { Log.d(TAG, "Marked badge as seen: ${dismissed.id}") }
            .onFailure { e -> Log.e(TAG, "Failed to mark badge as seen: ${dismissed.id}", e) }
    }

    /**
     * Drops the in-memory queue without acking anything server-side. Used when the user leaves
     * the authenticated area (logout/account deletion) - safe, since this queue only mirrors
     * server-side is_seen state: whoever is authenticated next time gets a correct re-fetch,
     * never a leak of another session's pending celebration.
     */
    fun clear() {
        Log.d(TAG, "Clearing pending badge queue (had ${_pendingBadges.value.size} pending)")
        _pendingBadges.value = emptyList()
    }
}
