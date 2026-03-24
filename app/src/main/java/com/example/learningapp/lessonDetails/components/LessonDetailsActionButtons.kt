package com.example.learningapp.lessonDetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (lesson.isInProgress) {
            // Main Action: Resume
            Button(
                onClick = onResumeLesson,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp), // Taller, premium feel
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Outlined.PlayCircle, contentDescription = null, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(8.dp))
                Text("Resume Lesson", style = MaterialTheme.typography.titleMedium)
            }

            // Secondary Action: Restart (Outlined to show lower priority)
            OutlinedButton(
                onClick = onStartLesson,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Outlined.Replay, contentDescription = null, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(8.dp))
                Text("Restart from beginning", style = MaterialTheme.typography.titleMedium)
            }
        } else {
            // Main Action: Start
            Button(
                onClick = onStartLesson,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Start Lesson", style = MaterialTheme.typography.titleMedium)
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