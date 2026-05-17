package com.example.learningapp.lessonProgress.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

/**
 * A highly efficient custom Canvas that draws sound waves based on microphone amplitude.
 * Using Canvas avoids the overhead of managing multiple Compose nodes (like Boxes),
 * making high-frequency 20fps animations run flawlessly on any device.
 *
 * @param amplitudeProgress A normalized float between 0.0f (quiet) and 1.0f (loud).
 * @param color The color of the sound wave bars.
 */
@Composable
fun AudioVisualizer(
    amplitudeProgress: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    // Smooth the jumping values from the microphone for a fluid wave effect
    val animatedProgress by animateFloatAsState(
        targetValue = amplitudeProgress,
        animationSpec = tween(durationMillis = 50),
        label = "VisualizerAnimation"
    )

    Canvas(modifier = modifier) {
        val centerY = size.height / 2
        val maxBarHeight = size.height
        val minBarHeight = 8.dp.toPx() // The bars never completely disappear

        val barWidth = 6.dp.toPx()
        val spacing = 6.dp.toPx()

        // We will draw 5 bars.
        val totalWidth = (barWidth * 5) + (spacing * 4)
        var startX = (size.width - totalWidth) / 2

        // Determine height for each of the 5 bars.
        // The center bar reacts the most (1.0x), the edges react less (0.4x).
        val heights = listOf(
            minBarHeight + (maxBarHeight * 0.4f * animatedProgress),
            minBarHeight + (maxBarHeight * 0.7f * animatedProgress),
            minBarHeight + (maxBarHeight * 1.0f * animatedProgress),
            minBarHeight + (maxBarHeight * 0.7f * animatedProgress),
            minBarHeight + (maxBarHeight * 0.4f * animatedProgress)
        )

        // Draw each bar with rounded ends
        heights.forEach { h ->
            drawLine(
                color = color,
                start = Offset(x = startX, y = centerY - (h / 2)),
                end = Offset(x = startX, y = centerY + (h / 2)),
                strokeWidth = barWidth,
                cap = StrokeCap.Round // Beautiful rounded ends for the bars
            )
            startX += barWidth + spacing
        }
    }
}