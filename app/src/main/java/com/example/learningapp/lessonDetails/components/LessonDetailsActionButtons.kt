package com.example.learningapp.lessonDetails.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learningapp.lessonDetails.LessonDetails

/**
 * The Call-To-Action buttons at the bottom of the screen.
 * Dynamically changes based on the lesson's progress state.
 */
@Composable
fun LessonDetailsActionButtons(
    lesson: LessonDetails,
    onStartLesson: () -> Unit,
    onResumeLesson: () -> Unit,
    modifier: Modifier = Modifier
) {
    // BEST PRACTICE: Use a Surface with tonal elevation to separate the bottom bar from the scrollable content
    Surface(
        modifier = modifier.fillMaxWidth(),
        tonalElevation = 8.dp, // Adds a subtle shadow/color change in M3
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                // NavigationBar/WindowInsets padding can be added here if needed
                .navigationBarsPadding()
        ) {
            if (lesson.isInProgress) {
                // SCENARIO A: Lesson is in progress
                // Primary Button -> Resume
                Button(
                    onClick = onResumeLesson,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Icon(Icons.Outlined.PlayCircle, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Resume Lesson", style = MaterialTheme.typography.labelLarge)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Secondary Button -> Restart (Low emphasis)
                TextButton(
                    onClick = onStartLesson, // Starting over
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Outlined.Replay, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Restart from beginning")
                }
            } else {
                // SCENARIO B: Lesson is brand new (or fully completed and needs a replay)
                Button(
                    onClick = onStartLesson,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Text("Start Lesson", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

// ==========================================
// PREVIEW
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
            completedSentences = 0 // isInProgress = false
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