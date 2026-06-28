package com.example.learningapp.lessonEnd.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FeedbackCard(feedbackText: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.AutoAwesome,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = feedbackText,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Start
            )
        }
    }
}

// ==========================================
// PREVIEW
// ==========================================

@Preview(showBackground = true, name = "1. score >= 90")
@Composable
fun FeedbackCardPreviewShort() {
    MaterialTheme {
        FeedbackCard(
            feedbackText = "Excellent pronunciation! You sound like a native speaker. Keep up the amazing work!",
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, name = "2. score >= 75")
@Composable
fun FeedbackCardPreviewLong() {
    MaterialTheme {
        FeedbackCard(
            feedbackText = "Great job! Your fluency and pronunciation are getting much better. Keep it up!",
            modifier = Modifier.padding(16.dp)
        )
    }
}