package com.example.learningapp.progress.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learningapp.core.UiState
import com.example.learningapp.progress.Badge
import com.example.learningapp.ui.components.ErrorStateComponent

@Composable
fun BadgesTabContent(
    state: UiState<List<Badge>>,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            is UiState.Idle, is UiState.Loading -> {
                CircularProgressIndicator()
            }
            is UiState.Error -> {
                ErrorStateComponent(
                    message = state.message,
                    onRetry = onRetry
                )
            }
            is UiState.Success -> {
                BadgesSuccessContent(badges = state.data)
            }
        }
    }
}

/**
 * Displays the grid of badges.
 */
@Composable
private fun BadgesSuccessContent(
    badges: List<Badge>,
    modifier: Modifier = Modifier
) {
    var selectedBadge by remember { mutableStateOf<Badge?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = badges,
                key = { badge -> badge.id }
            ) { badge ->
                BadgeItem(
                    badge = badge,
                    onClick = { selectedBadge = badge }
                )
            }
        }

        selectedBadge?.let { badge ->
            BadgeDetailDialog(
                badge = badge,
                onDismissRequest = { selectedBadge = null }
            )
        }
    }
}

// ==========================================
// PREVIEW
// ==========================================
@Preview(showBackground = true, name = "Badges Tab - Light Mode")
@Composable
fun BadgesTabPreview() {
    val mockBadges = listOf(
        Badge(
            id = "b1",
            title = "First Step",
            description = "Completed your very first lesson.",
            isAchieved = true
        ),
        Badge(
            id = "b2",
            title = "10 Days Streak",
            description = "Learned for 10 consecutive days.",
            isAchieved = true
        ),
        Badge(
            id = "b3",
            title = "Perfect Score",
            description = "Completed a lesson with 100% accuracy.",
            isAchieved = false
        ),
        Badge(
            id = "b4",
            title = "Globetrotter",
            description = "Completed the 'Trip Abroad' category.",
            isAchieved = false
        )
    )

    MaterialTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            BadgesSuccessContent(badges = mockBadges)
        }
    }
}