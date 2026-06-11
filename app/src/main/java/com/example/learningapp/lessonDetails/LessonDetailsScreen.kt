package com.example.learningapp.lessonDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.learningapp.core.UiState
import com.example.learningapp.lessonDetails.components.LessonDetailsActionButtons
import com.example.learningapp.lessonDetails.components.LessonDetailsHeader
import com.example.learningapp.lessonDetails.components.LessonDetailsInfoCard
import com.example.learningapp.ui.components.ErrorStateComponent

@Composable
fun LessonDetailsScreen(
    viewModel: LessonDetailsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    // Callbacks to navigate to the actual player screen
    onNavigateToLessonPlayer: (lessonId: String, startIndex: Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LessonDetailsContent(
        state = uiState,
        onBackClick = onBackClick,
        onStartLesson = { lessonId ->
            // Start from index 0
            onNavigateToLessonPlayer(lessonId, 0)
        },
        onResumeLesson = { lessonId, completedCount ->
            // Resume from the exact sentence index they left off
            onNavigateToLessonPlayer(lessonId, completedCount)
        },
        onRetry = {
            viewModel.loadLessonDetails()
        }
    )
}

// ==========================================
// STATELESS UI COMPONENT
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonDetailsContent(
    state: UiState<LessonDetails>,
    onBackClick: () -> Unit,
    onStartLesson: (String) -> Unit,
    onResumeLesson: (String, Int) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Navigate Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background // Consistent background
                )
            )
        }
        // Notice: bottomBar is COMPLETELY REMOVED!
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is UiState.Idle, is UiState.Loading -> {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
                is UiState.Error -> {
                    ErrorStateComponent(
                        message = state.message,
                        onRetry = onRetry
                    )
                }
                is UiState.Success -> {
                    val lesson = state.data

                    // Scrollable content area now contains EVERYTHING
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // 1. The Header
                        LessonDetailsHeader(lesson = lesson)

                        // 2. The Upgraded Info Card
                        LessonDetailsInfoCard(lesson = lesson)

                        // 3. The Action Buttons (Now flow naturally with the content!)
                        LessonDetailsActionButtons(
                            lesson = lesson,
                            onStartLesson = { onStartLesson(lesson.id) },
                            onResumeLesson = { onResumeLesson(lesson.id, lesson.completedSentences) }
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// PREVIEW
// ==========================================

// --- Mock Data ---
private val mockNewLesson = LessonDetails(
    id = "l1_1",
    title = "At the Airport",
    description = "Learn essential vocabulary for checking in, dropping off bags, and going through security without breaking a sweat.",
    sentencesCount = 12,
    completedSentences = 0 // Brand-new lesson
)

private val mockInProgressLesson = LessonDetails(
    id = "l1_2",
    title = "Job Interview Basics",
    description = "Master professional language and soft skills for successful job interviews in English. Make a great first impression.",
    sentencesCount = 20,
    completedSentences = 8 // In progress
)

// --- Previews ---

@Preview(showBackground = true, name = "1. Full Screen - New Lesson")
@Composable
fun LessonDetailsContentNewPreview() {
    MaterialTheme {
        LessonDetailsContent(
            state = UiState.Success(mockNewLesson),
            onBackClick = {},
            onStartLesson = {},
            onResumeLesson = { _, _ -> },
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, name = "2. Full Screen - In Progress")
@Composable
fun LessonDetailsContentInProgressPreview() {
    MaterialTheme {
        LessonDetailsContent(
            state = UiState.Success(mockInProgressLesson),
            onBackClick = {},
            onStartLesson = {},
            onResumeLesson = { _, _ -> },
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, name = "3. Full Screen - Loading")
@Composable
fun LessonDetailsContentLoadingPreview() {
    MaterialTheme {
        LessonDetailsContent(
            state = UiState.Loading,
            onBackClick = {},
            onStartLesson = {},
            onResumeLesson = { _, _ -> },
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, name = "4. Full Screen - Error")
@Composable
fun LessonDetailsContentErrorPreview() {
    MaterialTheme {
        LessonDetailsContent(
            state = UiState.Error("Failed to fetch lesson data. Please check your internet connection."),
            onBackClick = {},
            onStartLesson = {},
            onResumeLesson = { _, _ -> },
            onRetry = {}
        )
    }
}