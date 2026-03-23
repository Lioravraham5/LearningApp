package com.example.learningapp.progress.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learningapp.core.UiState
import com.example.learningapp.progress.CategoryAchievement
import com.example.learningapp.ui.components.ErrorStateComponent

/**
 * Entry point for the Categories / Achievements Tab.
 * Manages the UI State (Loading, Error, Success).
 */
@Composable
fun CategoriesAchievementsTabContent(
    state: UiState<List<CategoryAchievement>>,
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
                AchievementsSuccessContent(categories = state.data)
            }
        }
    }
}

/**
 * The flat, highly-optimized LazyColumn layout for the Categories list.
 */
@Composable
private fun AchievementsSuccessContent(
    categories: List<CategoryAchievement>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp) // Space between cards
    ) {
        items(
            items = categories,
            key = { category -> category.categoryId } // Best practice: unique key for performance
        ) { category ->
            CategoryAchievementItem(achievement = category)
        }

        // Optional: Bottom spacer for navigation bar safety
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ==========================================
// PREVIEW
// ==========================================
@Preview(showBackground = true, name = "Achievements Tab - Light Mode")
@Composable
fun AchievementsTabPreview() {
    val mockData = listOf(
        CategoryAchievement(
            categoryId = "1",
            categoryName = "Trip Abroad",
            averageScore = 92,
            completedLessons = 8,
            inProgressLessons = 2,
            unDoneLessons = 5,
            totalLessons = 15 // 8 + 2 + 5 = 15
        ),
        CategoryAchievement(
            categoryId = "2",
            categoryName = "Job Interview",
            averageScore = 78,
            completedLessons = 5,
            inProgressLessons = 4,
            unDoneLessons = 11,
            totalLessons = 20 // 5 + 4 + 11 = 20
        ),
        CategoryAchievement(
            categoryId = "3",
            categoryName = "Daily Life",
            averageScore = 100,
            completedLessons = 10,
            inProgressLessons = 0,
            unDoneLessons = 0,
            totalLessons = 10 // 10 + 0 + 0 = 10 (100% progress)
        )
    )

    MaterialTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            AchievementsSuccessContent(categories = mockData)
        }
    }
}