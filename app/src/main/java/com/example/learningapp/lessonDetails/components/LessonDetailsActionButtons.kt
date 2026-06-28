package com.example.learningapp.lessonDetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learningapp.lessonDetails.LessonDetails

/**
 * The Call-To-Action buttons at the bottom of the screen.
 * Dynamically changes based on the lesson's active run progress state.
 */
@Composable
fun LessonDetailsActionButtons(
    lesson: LessonDetails,
    onStartLesson: () -> Unit,
    onResumeLesson: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        when {
            // Scenario 1: First time playing or zero progress in the current run
            lesson.isNotStarted -> {
                Button(
                    onClick = onStartLesson, // Triggers index 0 -> API will generate a NEW run_id
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp), // Premium feel
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Start Lesson", style = MaterialTheme.typography.titleMedium)
                }
            }

            // Scenario 2: User is in the middle of a lesson
            lesson.isInProgress -> {
                // Main Action: Resume
                Button(
                    onClick = onResumeLesson, // Triggers completedSentences index -> API reuses active run_id
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Outlined.PlayCircle, contentDescription = null, modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Resume Lesson", style = MaterialTheme.typography.titleMedium)
                }

                // Secondary Action: Restart (Outlined to show lower priority)
                OutlinedButton(
                    onClick = onStartLesson, // Triggers index 0 -> API will overwrite current run with a NEW run_id
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Outlined.Replay, contentDescription = null, modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Restart from beginning", style = MaterialTheme.typography.titleMedium)
                }
            }

            // Scenario 3: User finished the active run successfully
            lesson.isCompleted -> {
                Button(
                    onClick = onStartLesson, // Triggers index 0 -> API will generate a NEW run_id
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Outlined.Replay, contentDescription = null, modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Practice Again", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

// ==========================================
// PREVIEWS
// ==========================================

@Preview(showBackground = true, name = "1. Action Buttons - Not Started")
@Composable
fun LessonDetailsActionButtonsNotStartedPreview() {
    MaterialTheme {
        val mockLesson = LessonDetails(
            id = "mock_1",
            title = "Ordering Food",
            description = "Learn how to order in a restaurant.",
            sentencesCount = 10,
            completedSentences = 0 // isNotStarted = true
        )

        LessonDetailsActionButtons(
            lesson = mockLesson,
            onStartLesson = { /* Preview: Do nothing */ },
            onResumeLesson = { /* Preview: Do nothing */ }
        )
    }
}

@Preview(showBackground = true, name = "2. Action Buttons - In Progress")
@Composable
fun LessonDetailsActionButtonsInProgressPreview() {
    MaterialTheme {
        val mockLesson = LessonDetails(
            id = "mock_2",
            title = "Ordering Food",
            description = "Learn how to order in a restaurant.",
            sentencesCount = 10,
            completedSentences = 4 // isInProgress = true
        )

        LessonDetailsActionButtons(
            lesson = mockLesson,
            onStartLesson = { /* Preview: Do nothing */ },
            onResumeLesson = { /* Preview: Do nothing */ }
        )
    }
}

@Preview(showBackground = true, name = "3. Action Buttons - Completed")
@Composable
fun LessonDetailsActionButtonsCompletedPreview() {
    MaterialTheme {
        val mockLesson = LessonDetails(
            id = "mock_3",
            title = "Ordering Food",
            description = "Learn how to order in a restaurant.",
            sentencesCount = 10,
            completedSentences = 10 // isCompleted = true
        )

        LessonDetailsActionButtons(
            lesson = mockLesson,
            onStartLesson = { /* Preview: Do nothing */ },
            onResumeLesson = { /* Preview: Do nothing */ }
        )
    }
}