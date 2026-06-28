package com.example.learningapp.lessonEnd.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HeroScoreRing(score: Int, modifier: Modifier = Modifier) {
    // Target progress calculation (0.0 to 1.0)
    val targetProgress = score / 100f

    // Smooth animation filling the ring over 1 second
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 1000),
        label = "Score Animation"
    )

    // Determine color based on score thresholds
    val scoreColor = when {
        score >= 80 -> Color(0xFF4CAF50) // Green for excellent
        score >= 50 -> Color(0xFFFF9800) // Orange for average
        else -> MaterialTheme.colorScheme.error // Red/Error color for low score
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(200.dp)
    ) {
        // Background track (faint)
        CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surfaceVariant,
            strokeWidth = 12.dp
        )

        // Foreground animated progress
        CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxSize(),
            color = scoreColor,
            strokeWidth = 12.dp
        )

        // Inner Text
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$score%",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = scoreColor
                )
            )
            Text(
                text = "Score",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ==========================================
// PREVIEW
// ==========================================

@Preview(showBackground = true, name = "1. High Score (Excellent)")
@Composable
fun HeroScoreRingPreviewHigh() {
    MaterialTheme {
        HeroScoreRing(score = 85, modifier = Modifier.padding(16.dp))
    }
}

@Preview(showBackground = true, name = "2. Medium Score (Average)")
@Composable
fun HeroScoreRingPreviewMedium() {
    MaterialTheme {
        HeroScoreRing(score = 65, modifier = Modifier.padding(16.dp))
    }
}

@Preview(showBackground = true, name = "3. Low Score (Error)")
@Composable
fun HeroScoreRingPreviewLow() {
    MaterialTheme {
        HeroScoreRing(score = 30, modifier = Modifier.padding(16.dp))
    }
}