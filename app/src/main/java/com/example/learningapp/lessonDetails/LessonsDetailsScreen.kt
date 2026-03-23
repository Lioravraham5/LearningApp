package com.example.learningapp.lessonDetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class) // Required for TopAppBar
@Composable
fun LessonDetailsScreen(
    lessonId: String?,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* Empty title, we will show the title in the header */ },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Navigate Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Navigated to Lesson ID: $lessonId",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

// ==========================================
// PREVIEW
// ==========================================
@Preview(showBackground = true, name = "1. Lesson Details - With ID")
@Composable
fun LessonDetailsScreenPreview() {
    MaterialTheme {
        LessonDetailsScreen(
            lessonId = "l1_1", // Simulating a valid passed ID
            onBackClick = { /* Preview: Do nothing */ }
        )
    }
}

@Preview(showBackground = true, name = "2. Lesson Details - Null ID")
@Composable
fun LessonDetailsScreenNullPreview() {
    MaterialTheme {
        LessonDetailsScreen(
            lessonId = null, // Simulating a case where the ID is missing
            onBackClick = { /* Preview: Do nothing */ }
        )
    }
}