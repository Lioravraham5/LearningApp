package com.example.learningapp.badgeCelebration.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.learningapp.badgeCelebration.ui.components.BadgeCelebrationDialog

private const val TAG = "BadgeCelebrationHost"

/**
 * Self-contained global host for the badge-celebration overlay. Mounted once in MainAppScreen as
 * a sibling to the app's Scaffold, so it can render above any MAIN-graph screen regardless of
 * which one is currently showing.
 *
 * [isActive] must be true only while the user is authenticated and inside Graph. MAIN - it gates
 * the fetch triggers (never fires on login/register, would otherwise 401), the pending queue
 * itself (cleared when leaving the MAIN graph, e.g. logout), and the dialog's rendering (never
 * shown outside the MAIN graph even if something is still queued at the exact moment of the
 * transition - the underlying BadgeCelebrationCoordinator is a process-wide singleton with no
 * concept of auth state, so this check has to happen here).
 */
@Composable
fun BadgeCelebrationHost(
    isActive: Boolean,
    viewModel: BadgeCelebrationViewModel = hiltViewModel()
) {
    val celebration by viewModel.currentCelebration.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(isActive) {
        if (isActive) {
            Log.d(TAG, "isActive=true, checking for unseen badges")
            viewModel.checkForUnseenBadges()
        } else {
            // Leaving the authenticated area (logout, account deletion, or simply not there yet).
            // Safe to drop: this queue only mirrors server-side is_seen state, so whoever is
            // authenticated next time isActive flips true gets a correct re-fetch, never a leak
            // of another session's pending celebration.
            Log.d(TAG, "isActive=false, clearing pending celebrations")
            viewModel.clearPendingCelebrations()
        }
    }

    DisposableEffect(lifecycleOwner, isActive) {
        val observer = LifecycleEventObserver { _, event ->
            if (isActive && event == Lifecycle.Event.ON_RESUME) {
                Log.d(TAG, "[LIFECYCLE] ON_RESUME with isActive=true, re-checking for unseen badges")
                viewModel.checkForUnseenBadges()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    if (isActive) {
        celebration?.let { badge ->
            BadgeCelebrationDialog(badge = badge, onDismiss = viewModel::dismissCurrentCelebration)
        }
    }
}