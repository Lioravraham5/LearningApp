package com.example.learningapp.categoryDetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learningapp.categoryDetails.Lesson
import com.example.learningapp.categoryDetails.LessonDifficulty

/**
 * A stateless, modern card component displaying a single lesson's info.
 */
@Composable
fun LessonItem(
    lesson: Lesson,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    // 1. Determine the color of the difficulty badge based on the enum
    val difficultyColor = when (lesson.difficulty) {
        LessonDifficulty.EASY -> Color(0xFF4CAF50) // Green
        LessonDifficulty.MEDIUM -> Color(0xFFFF9800) // Orange
        LessonDifficulty.HARD -> MaterialTheme.colorScheme.error // Red from theme
    }

    // Modern Material 3 Elevated Card for a subtle shadow effect
    ElevatedCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // --- Top Row: Lesson Title and Difficulty Badge ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Lesson Title (takes available space, leaves room for the badge)
                Text(
                    text = lesson.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Difficulty Badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = difficultyColor.copy(alpha = 0.1f), // Light background
                    contentColor = difficultyColor // Vibrant text
                ) {
                    Text(
                        text = lesson.difficulty.name, // e.g., "EASY", "MEDIUM"
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // --- Bottom Row: Progress Bar and Text ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // The backend sends a value between 0.0 and 1.0 (e.g., 0.5), LinearProgressIndicator expects exactly this format.
                LinearProgressIndicator(
                    progress = { lesson.progressPercentage },
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = StrokeCap.Round
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Multiply by 100 before casting to Int to get the correct percentage text
                val percentageText = (lesson.progressPercentage * 100).toInt()
                Text(
                    text = "$percentageText%",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ==========================================
// PREVIEW
// ==========================================

@Preview(showBackground = true, name = "Lesson Items List")
@Composable
fun LessonItemPreview() {
    // Mock data showcasing different difficulties and progress levels
    val mockLessons = listOf(
        Lesson(
            id = "1",
            title = "Basic Greetings",
            progressPercentage = 100f, // 100% completed
            difficulty = LessonDifficulty.EASY
        ),
        Lesson(
            id = "2",
            title = "Present Tense Verbs",
            progressPercentage = 45f, // 45% completed
            difficulty = LessonDifficulty.MEDIUM
        ),
        Lesson(
            id = "3",
            title = "Advanced Past Perfect Subjunctive", // Long title to test truncation
            progressPercentage = 10f,  // 10% completed
            difficulty = LessonDifficulty.HARD
        )
    )

    MaterialTheme {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Render each mock lesson
                mockLessons.forEach { lesson ->
                    LessonItem(
                        lesson = lesson,
                        onClick = { /* Preview: Do nothing */ }
                    )
                }
            }
        }
    }
}