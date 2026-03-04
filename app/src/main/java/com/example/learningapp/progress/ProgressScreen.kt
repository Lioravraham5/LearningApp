package com.example.learningapp.progress

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.learningapp.core.UiState
import com.example.learningapp.progress.components.ProgressHeader
import kotlinx.coroutines.launch

/**
 * Stateful entry point for the Progress Screen.
 * Responsible for managing the ViewModel and collecting the UI state.
 */
@Composable
fun ProgressScreen(
    viewModel: ProgressViewModel = hiltViewModel()
) {
    // Collect the state in a lifecycle-aware manner
    val state by viewModel.progressState.collectAsStateWithLifecycle()

    // Pass the state and actions to the stateless component
    ProgressScreenContent(
        state = state,
        onTabSelected = { index ->
            viewModel.onTabSelected(index)
        }
    )
}

/**
 * Stateless UI component for the Progress Screen.
 * Purely declarative, making it easy to test and preview.
 */
@Composable
fun ProgressScreenContent(
    state: ProgressState,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // 1. Define the tabs
    val tabs = listOf("Overview", "Categories", "Badges")

    // 2. Initialize the Pager state
    val pagerState = rememberPagerState(pageCount = { tabs.size })

    // 3. Coroutine scope for animating pager scrolls on tab clicks
    val coroutineScope = rememberCoroutineScope()

    // 4. THE LAZY LOADING TRIGGER
    // This side-effect listens to the current page. Whenever the user swipes or clicks to a new tab,
    // it tells the ViewModel to fetch data (if it hasn't been fetched yet).
    LaunchedEffect(pagerState.currentPage) {
        onTabSelected(pagerState.currentPage)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp) // Removed horizontal padding here to let tabs span full width
    ) {
        // --- Static Header ---
        ProgressHeader(
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        SecondaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        // Animate scroll to the clicked tab
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        // --- Horizontal Pager ---
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            // Switch statement to show the correct content based on the current page
            when (page) {
                0 -> OverviewTabContent(state = state.overviewState)
                1 -> AchievementsTabContent(state = state.achievementsState)
                2 -> BadgesTabContent(state = state.badgesState)
            }
        }
    }
}


// STEP 5 PLACEHOLDERS - These handle the UI states (Loading, Success, Error)
@Composable
fun OverviewTabContent(state: UiState<OverviewData>) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (state) {
            is UiState.Idle, is UiState.Loading -> CircularProgressIndicator()
            is UiState.Error -> Text(text = state.message, color = MaterialTheme.colorScheme.error)
            is UiState.Success -> Text(text = "Overview Loaded: ${state.data.averageScore}% Avg")
        }
    }
}

@Composable
fun AchievementsTabContent(state: UiState<List<CategoryAchievement>>) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (state) {
            is UiState.Idle, is UiState.Loading -> CircularProgressIndicator()
            is UiState.Error -> Text(text = state.message, color = MaterialTheme.colorScheme.error)
            is UiState.Success -> Text(text = "Achievements Loaded: ${state.data.size} Categories")
        }
    }
}

@Composable
fun BadgesTabContent(state: UiState<List<Badge>>) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (state) {
            is UiState.Idle, is UiState.Loading -> CircularProgressIndicator()
            is UiState.Error -> Text(text = state.message, color = MaterialTheme.colorScheme.error)
            is UiState.Success -> Text(text = "Badges Loaded: ${state.data.size} Badges")
        }
    }
}

@Preview(showBackground = true, name = "Progress Screen - Overview Tab")
@Composable
fun ProgressScreenPreview() {
    // Create a mock state to represent what the UI should show
    val mockState = ProgressState(
        overviewState = UiState.Success(
            OverviewData(
                averageScore = 85,
                totalCompletedLessons = 25,
                totalEarnedBadges = 10,
                dailyStreak = 14
            )
        ),
        achievementsState = UiState.Idle, // Other tabs stay Idle for now
        badgesState = UiState.Idle
    )

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ProgressScreenContent(
                state = mockState,
                onTabSelected = { /* Do nothing in preview */ }
            )
        }
    }
}