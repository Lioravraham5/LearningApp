package com.example.learningapp.lessonDetails.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learningapp.lessonDetails.LessonDetails

/**
 * Displays metadata about the lesson.
 * Upgraded to a modern M3 OutlinedCard with a visual Progress Bar.
 */
@Composable
fun LessonDetailsInfoCard(
    lesson: LessonDetails,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Premium Icon Container
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.FormatListBulleted,
                        contentDescription = "Sentences Count",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Text Content
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${lesson.sentencesCount} Sentences",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Dynamic Subtitle
                    Text(
                        text = if (lesson.isInProgress) {
                            "You've completed ${lesson.completedSentences} so far"
                        } else {
                            "Ready to start learning"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Visual Progress Bar (Only shows if started)
            if (lesson.isInProgress) {
                Spacer(modifier = Modifier.height(16.dp))

                // Safe calculation to prevent division by zero
                val progress = if (lesson.sentencesCount > 0) {
                    lesson.completedSentences.toFloat() / lesson.sentencesCount.toFloat()
                } else {
                    0f
                }

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)), // Rounded edges for the bar
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primaryContainer,
                )
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