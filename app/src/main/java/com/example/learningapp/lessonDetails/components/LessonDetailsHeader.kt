package com.example.learningapp.lessonDetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learningapp.lessonDetails.LessonDetails

/**
 * A helper component specifically for the top section of the lesson details screen.
 * Adapted to perfectly match the design language of CategoryDetailsHeader.
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
        // Large Circular Icon Background (M3 styling)
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = lesson.iconUrl),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(48.dp)
            )
        }

        // Lesson Title
        Text(
            text = lesson.title,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        // Lesson Description
        Text(
            text = lesson.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

// ==========================================
// PREVIEW
// ==========================================

@Preview(showBackground = true, name = "Lesson Details Header - Light Mode")
@Composable
fun LessonDetailsHeaderPreview() {
    MaterialTheme {
        // Surface provides the default Material background color
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
                modifier = Modifier.padding(vertical = 24.dp)
            )
        }
    }
}