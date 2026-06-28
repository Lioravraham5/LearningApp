package com.example.learningapp.lessonDetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learningapp.lessonDetails.LessonDetails

/**
 * A typography-focused header component for the lesson details screen.
 * Uses a subtle "overline" badge to anchor the visual hierarchy instead of an icon.
 */
@Composable
fun LessonDetailsHeader(
    lesson: LessonDetails,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Visual Anchor (Badge/Chip) instead of an icon
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ) {
            Text(
                text = "LESSON",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                letterSpacing = 1.5.sp // Tracking out the letters gives a premium, editorial look
            )
        }

        // 2. Lesson Title
        Text(
            text = lesson.title,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // 3. Lesson Description
        Text(
            text = lesson.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp) // Slightly tighter padding for better text wrapping
        )
    }
}

// ==========================================
// PREVIEW
// ==========================================

@Preview(showBackground = true, name = "Lesson Details Header - Typography Driven")
@Composable
fun LessonDetailsHeaderPreview() {
    MaterialTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val mockLesson = LessonDetails(
                id = "mock_l1",
                title = "Introduction to Greetings",
                description = "Learn the basics of saying hello, goodbye, and introducing yourself in everyday casual conversations.",
                sentencesCount = 10,
                completedSentences = 3
            )

            LessonDetailsHeader(
                lesson = mockLesson,
                modifier = Modifier.padding(vertical = 32.dp)
            )
        }
    }
}