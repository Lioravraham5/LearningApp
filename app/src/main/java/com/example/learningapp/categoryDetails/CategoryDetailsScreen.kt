package com.example.learningapp.categoryDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.learningapp.categoryDetails.components.CategoryDetailsHeader
import com.example.learningapp.categoryDetails.components.LessonItem
import com.example.learningapp.core.UiState

/**
 * STATEFUL ENTRY POINT:
 * Connects the UI to the ViewModel. Collects the state and passes it down.
 */
@Composable
fun CategoryDetailsScreen(
    viewModel: CategoryDetailsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNavigateToLesson: (String) -> Unit
) {
    // Collect the state in a lifecycle-aware manner
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CategoryDetailsContent(
        state = uiState,
        onBackClick = onBackClick,
        onNavigateToLesson = onNavigateToLesson
    )
}

/**
 * STATELESS UI COMPONENT:
 * Purely responsible for rendering the UI based on the provided state.
 * Highly testable and easy to preview.
 */
@OptIn(ExperimentalMaterial3Api::class) // Required for TopAppBar
@Composable
fun CategoryDetailsContent(
    state: UiState<CategoryDetails>,
    onBackClick: () -> Unit,
    onNavigateToLesson: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Scaffold provides the modern Material 3 structure (TopBar + Content Area)
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { /* Empty title, we will show the title in the header */ },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Navigate Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->

        // Handle the different UI States gracefully
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
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
                    val category = state.data

                    // Main list containing the Header and the Lessons
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        // 1. The Header Section (Icon, Title, Description)
                        item {
                            CategoryDetailsHeader(category = category)
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // 2. The Title for the lessons list
                        item {
                            Text(
                                text = "Lessons",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        // 3. The List of Lessons
                        items(
                            items = category.lessons,
                            key = { lesson -> lesson.id } // Performance boost
                        ) { lesson ->
                            LessonItem(
                                lesson = lesson,
                                onClick = {
                                    onNavigateToLesson(lesson.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- Mock Data for Previews ---
private val mockLessonsPreview = listOf(
    Lesson(
        id = "1",
        title = "Basic Greetings",
        progressPercentage = 1.0f,
        difficulty = LessonDifficulty.EASY
    ),
    Lesson(
        id = "2",
        title = "Present Tense Verbs",
        progressPercentage = 0.45f,
        difficulty = LessonDifficulty.MEDIUM
    ),
    Lesson(
        id = "3",
        title = "Advanced Past Perfect Subjunctive",
        progressPercentage = 0.0f,
        difficulty = LessonDifficulty.HARD
    )
)

private val mockCategoryDetailsPreview = CategoryDetails(
    id = "cat_1",
    title = "Advanced Grammar",
    description = "Master the complexities of verb conjugations, sentence structures, and advanced tenses to speak like a native.",
    lessons = mockLessonsPreview
)

// --- Previews ---

@Preview(showBackground = true, name = "1. Category Details - Success State")
@Composable
fun CategoryDetailsContentSuccessPreview() {
    MaterialTheme {
        CategoryDetailsContent(
            // Passing the Success state with our mock data
            state = UiState.Success(mockCategoryDetailsPreview),
            onBackClick = { /* Preview: Do nothing */ },
            onNavigateToLesson = { /* Preview: Do nothing */ }
        )
    }
}

@Preview(showBackground = true, name = "2. Category Details - Loading State")
@Composable
fun CategoryDetailsContentLoadingPreview() {
    MaterialTheme {
        CategoryDetailsContent(
            // Passing the Loading state
            state = UiState.Loading,
            onBackClick = { /* Preview: Do nothing */ },
            onNavigateToLesson = { /* Preview: Do nothing */ }
        )
    }
}

@Preview(showBackground = true, name = "3. Category Details - Error State")
@Composable
fun CategoryDetailsContentErrorPreview() {
    MaterialTheme {
        CategoryDetailsContent(
            // Passing the Error state with a mock message
            state = UiState.Error("Failed to load category. Please check your internet connection and try again."),
            onBackClick = { /* Preview: Do nothing */ },
            onNavigateToLesson = { /* Preview: Do nothing */ }
        )
    }
}
