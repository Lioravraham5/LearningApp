package com.example.learningapp.lessonDetails.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learningapp.lessonDetails.LessonDetails

/**
 * Displays metadata about the lesson, like the number of sentences.
 * Uses Material 3 ElevatedCard for a modern surface look.
 */
@Composable
fun LessonDetailsInfoCard(
    lesson: LessonDetails,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon representing the content (e.g., a list of sentences)
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.FormatListBulleted,
                contentDescription = "Sentences Count",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "${lesson.sentencesCount} Sentences",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                // If the user has started, show their progress in text
                if (lesson.isInProgress) {
                    Text(
                        text = "You've completed ${lesson.completedSentences} so far",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// ==========================================
// PREVIEW
// ==========================================

@Preview(showBackground = true, name = "1. Lesson Info - Not Started")
@Composable
fun LessonDetailsInfoCardNotStartedPreview() {
    MaterialTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val mockLesson = LessonDetails(
                id = "mock_1",
                title = "Ordering Food",
                description = "Learn how to order in a restaurant.",
                sentencesCount = 15,
                completedSentences = 0 // isInProgress = false
            )

            LessonDetailsInfoCard(
                lesson = mockLesson,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "2. Lesson Info - In Progress")
@Composable
fun LessonDetailsInfoCardInProgressPreview() {
    MaterialTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val mockLesson = LessonDetails(
                id = "mock_2",
                title = "Ordering Food",
                description = "Learn how to order in a restaurant.",
                sentencesCount = 15,
                completedSentences = 6 // isInProgress = true
            )

            LessonDetailsInfoCard(
                lesson = mockLesson,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}