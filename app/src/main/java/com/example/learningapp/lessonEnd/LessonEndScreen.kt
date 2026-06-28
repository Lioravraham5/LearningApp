package com.example.learningapp.lessonEnd

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.learningapp.core.UiState
import com.example.learningapp.lessonEnd.components.ActionButtons
import com.example.learningapp.lessonEnd.components.FeedbackCard
import com.example.learningapp.lessonEnd.components.HeroScoreRing
import com.example.learningapp.lessonEnd.models.LessonCompleteResponse
import com.example.learningapp.lessonEnd.models.ProgressStatus
import com.example.learningapp.ui.components.ErrorStateComponent

/**
 * Stateful entry point for the Lesson End Screen.
 */
@Composable
fun LessonEndScreen(
    viewModel: LessonEndViewModel = hiltViewModel(),
    onNavigateToCategoryDetails: () -> Unit,
    onPracticeAgain: (lessonId: String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is UiState.Idle, is UiState.Loading -> {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }

            is UiState.Error -> {
                // Using your robust, reusable ErrorStateComponent
                ErrorStateComponent(
                    message = state.message,
                    onRetry = { viewModel.finalizeLesson() }
                )
            }

            is UiState.Success -> {
                LessonEndContent(
                    response = state.data,
                    onContinueClick = onNavigateToCategoryDetails,
                    onPracticeAgainClick = { onPracticeAgain(state.data.lessonId) }
                )
            }
        }
    }
}

/**
 * Stateless UI component for the Lesson End Screen.
 * Elements now flow naturally with a scrollable column, rather than being pinned to the bottom.
 */
@Composable
fun LessonEndContent(
    response: LessonCompleteResponse,
    onContinueClick: () -> Unit,
    onPracticeAgainClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Scrollable content area contains EVERYTHING and flows naturally
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 48.dp), // Safe padding from screen edges
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.dp) // Consistent spacing between all items!
    ) {
        // 1. The Hero Element (Score Ring)
        HeroScoreRing(score = response.averageScore)

        // 2. Dynamic Feedback Card
        FeedbackCard(feedbackText = response.feedbackText)

        // 3. Call to Action Buttons (Flow naturally right after the feedback)
        ActionButtons(
            onContinueClick = onContinueClick,
            onPracticeAgainClick = onPracticeAgainClick
        )
    }
}

// ==========================================
// PREVIEWS
// ==========================================

@Preview(showBackground = true)
@Composable
fun LessonEndContentPreview() {
    MaterialTheme {
        LessonEndContent(
            response = LessonCompleteResponse(
                lessonId = "dummy_id",
                status = ProgressStatus.COMPLETED,
                averageScore = 85,
                feedbackText = "Excellent pronunciation! You sound like a native speaker. Keep up the amazing work!"
            ),
            onContinueClick = {},
            onPracticeAgainClick = {}
        )
    }
}