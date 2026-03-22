package com.example.learningapp.progress.components

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoGraph
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learningapp.core.UiState
import com.example.learningapp.progress.OverviewData
import com.example.learningapp.progress.RecentBadge

/**
 * Entry point for the Overview Tab.
 * Handles the UiState (Loading, Error, Success) and delegates rendering.
 */
@Composable
fun OverviewTabContent(
    state: UiState<OverviewData>,
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
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            is UiState.Success -> {
                // When successful, render the flat Grid layout
                OverviewSuccessContent(data = state.data)
            }
        }
    }
}

/**
 * The flat, highly-optimized Grid layout for the Overview tab.
 * Avoids nested scrolling issues by managing everything in a single LazyVerticalGrid.
 */
@Composable
private fun OverviewSuccessContent(
    data: OverviewData,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // Defines a 2-column grid
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // ==========================================
        // SECTION 1: STATS (2 items per row by default)
        // ==========================================
        item {
            StatCard(
                title = "Score",
                value = "${data.averageScore}%",
                icon = Icons.Rounded.AutoGraph,
                containerColor = Color(0xFFE3F2FD), // Light Blue 50
                contentColor = Color(0xFF1565C0),   // Dark Blue 800
                modifier = Modifier.fillMaxWidth(),
                description = "Average score"
            )
        }
        item {
            StatCard(
                title = "Completed",
                description = "Lessons",
                value = data.totalCompletedLessons.toString(),
                icon = Icons.Rounded.CheckCircle,
                containerColor = Color(0xFFE8F5E9), // Light Green 50
                contentColor = Color(0xFF2E7D32),   // Dark Green 800
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            StatCard(
                title = "Badges",
                value = data.totalEarnedBadges.toString(),
                icon = Icons.Rounded.Star,
                containerColor = Color(0xFFF3E5F5), // Light Purple 50
                contentColor = Color(0xFF7B1FA2),   // Dark Purple 700
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            StatCard(
                title = "Streak",
                description = "Days in a row",
                value = data.dailyStreak.toString(),
                icon = Icons.Rounded.LocalFireDepartment,
                containerColor = Color(0xFFFFF3E0), // Light Orange 50
                contentColor = Color(0xFFE65100),   // Dark Orange 900 (Your original choice)
                modifier = Modifier.fillMaxWidth()
            )
        }

        // ==========================================
        // SECTION 2: RECENT ACHIEVEMENTS (Full Width)
        // ==========================================
        if (data.recentAchievements.isNotEmpty()) {

            // 1. Spacer to separate stats from the list (Spans both columns)
            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 2. Section Title (Spans both columns)
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "Recent Achievements",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // 3. The actual items (Each item spans both columns to look like a standard list row)
            items(
                items = data.recentAchievements,
                key = { badge -> badge.id }, // Best practice: Provide a unique key for list items
                span = { GridItemSpan(maxLineSpan) }
            ) { badge ->
                RecentBadgeItem(
                    badge = badge,
                    // No need for explicit fillMaxWidth here as the GridItemSpan handles the width,
                    // but it's safe since you have it inside the component itself.
                )
            }
        }

        // 4. Bottom Spacer for safe area navigation bar (Spans both columns)
        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, name = "Overview Tab - Light Mode")
@Composable
fun OverviewSuccessContentPreview() {
    // 1. Create realistic mock data for the preview
    val mockData = OverviewData(
        averageScore = 88,
        totalCompletedLessons = 42,
        totalEarnedBadges = 15,
        dailyStreak = 7,
        recentAchievements = listOf(
            RecentBadge(
                id = "rb1",
                title = "10 Days Streak",
                earnedDate = "Today"
            ),
            RecentBadge(
                id = "rb2",
                title = "Perfect Score",
                earnedDate = "2 days ago"
            ),
            RecentBadge(
                id = "rb3",
                title = "First Step",
                earnedDate = "1 week ago"
            )
        )
    )

    // 2. Wrap the component in MaterialTheme and Surface to get the correct colors
    MaterialTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxSize()
        ) {
            OverviewSuccessContent(data = mockData)
        }
    }
}