package com.example.learningapp.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.learningapp.home.components.CategoryCard
import com.example.learningapp.home.components.HomeHeader

/**
 * Stateful entry point for the Home Screen.
 * Responsible for injecting the ViewModel and collecting the UI state.
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    // Collects the state in a lifecycle-aware manner.
    val state by viewModel.homeState.collectAsStateWithLifecycle()

    // Pass the collected state to the stateless UI component
    HomeScreenContent(state = state)
}

/**
 * Stateless UI component for the Home Screen.
 * Receives data (state) and callbacks, making it highly testable and previewable.
 */
@Composable
fun HomeScreenContent(
    state: HomeState,
    modifier: Modifier = Modifier
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
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error,
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
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
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
                                // TODO: Handle navigation to category details
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
                        iconRes = android.R.drawable.ic_menu_camera,
                        totalLessons = 8,
                        completedLessons = 2
                    ),
                    Category(
                        id = "2",
                        title = "Job Interview",
                        description = "Master professional language.",
                        iconRes = android.R.drawable.ic_menu_gallery,
                        totalLessons = 12,
                        completedLessons = 5
                    ),
                    Category(
                        id = "3",
                        title = "Socializing",
                        description = "Learn how to introduce yourself and make friends.",
                        iconRes = android.R.drawable.ic_menu_myplaces,
                        totalLessons = 10,
                        completedLessons = 7
                    ),
                    Category(
                        id = "4",
                        title = "At the Restaurant",
                        description = "Ordering food and talking to waiters.",
                        iconRes = android.R.drawable.ic_menu_view,
                        totalLessons = 6,
                        completedLessons = 0
                    ),
                    Category(
                        id = "5",
                        title = "Shopping",
                        description = "Master phrases for buying clothes and groceries.",
                        iconRes = android.R.drawable.ic_menu_manage,
                        totalLessons = 15,
                        completedLessons = 3
                    )
                ),
                error = null
            )
        )
    }
}