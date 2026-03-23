package com.example.learningapp.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.learningapp.home.components.CategoryCard
import com.example.learningapp.home.components.HomeHeader
import com.example.learningapp.ui.components.ErrorStateComponent

/**
 * Stateful entry point for the Home Screen.
 * Responsible for injecting the ViewModel and collecting the UI state.
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToCategory: (String) -> Unit
) {
    // Collects the state in a lifecycle-aware manner.
    val state by viewModel.homeState.collectAsStateWithLifecycle()

    // This triggers every time the user navigates back to the Home tab.
    LaunchedEffect(Unit) {
        viewModel.refreshUser()
    }

    // Pass the collected state to the stateless UI component
    HomeScreenContent(
        state = state,
        onCategoryClick = onNavigateToCategory,
        onRetry = { viewModel.loadHomeData() }
    )
}

/**
 * Stateless UI component for the Home Screen.
 * Receives data (state) and callbacks, making it highly testable and previewable.
 */
@Composable
fun HomeScreenContent(
    state: HomeState,
    modifier: Modifier = Modifier,
    onCategoryClick: (String) -> Unit,
    onRetry: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {

        // Handle Loading State
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
        // Handle Error State
        else if (state.error != null) {
            ErrorStateComponent(
                message = state.error,
                onRetry = onRetry,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        // Handle Success State (Show Data)
        else {

            // Main vertical layout
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // --- FIXED HEADER SECTION ---
                HomeHeader(userName = state.userName)

                // --- CATEGORIES LIST ---
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        top = 16.dp,
                        bottom = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp) // Spacing between each item
                ) {

                    // Items: The Category Cards
                    items(
                        items = state.categories,
                        key = { category -> category.id } // Improves rendering performance
                    ) { category ->
                        CategoryCard(
                            category = category,
                            onClick = {
                                onCategoryClick(category.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

// Preview using the Stateless component with mock data
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreenContent(
            state = HomeState(
                isLoading = false,
                userName = "Student Name",
                categories = listOf(
                    Category(
                        id = "1",
                        title = "Trip Abroad",
                        description = "Learn essential phrases for traveling.",
                        totalLessons = 8,
                        completedLessons = 2,
                        progressPercentage = 0.33f
                    ),
                    Category(
                        id = "2",
                        title = "Job Interview",
                        description = "Master professional language.",
                        totalLessons = 12,
                        completedLessons = 5,
                        progressPercentage = 0.4f
                    ),
                    Category(
                        id = "3",
                        title = "Socializing",
                        description = "Learn how to introduce yourself and make friends.",
                        totalLessons = 10,
                        completedLessons = 7,
                        progressPercentage = 0.7f
                    ),
                    Category(
                        id = "4",
                        title = "At the Restaurant",
                        description = "Ordering food and talking to waiters.",
                        totalLessons = 6,
                        completedLessons = 0,
                        progressPercentage = 0f
                    ),
                    Category(
                        id = "5",
                        title = "Shopping",
                        description = "Master phrases for buying clothes and groceries.",
                        totalLessons = 15,
                        completedLessons = 3,
                        progressPercentage = 0.2f
                    )
                ),
                error = null
            ),
            onCategoryClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, name = "Home Screen - Error State")
@Composable
fun HomeScreenErrorPreview() {
    MaterialTheme {
        HomeScreenContent(
            state = HomeState(
                isLoading = false,
                userName = "Liora",
                categories = emptyList(),
                error = "HTTP 401 Unauthorized"
            ),
            onCategoryClick = {},
            onRetry = {}
        )
    }
}