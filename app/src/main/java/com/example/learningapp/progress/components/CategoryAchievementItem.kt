package com.example.learningapp.progress.components

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.learningapp.progress.CategoryAchievement
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview

/**
 * A stateless, modern card component displaying the progress of a specific learning category.
 * Updated to show detailed stats: Completed, In Progress, and Locked.
 */
@Composable
fun CategoryAchievementItem(
    achievement: CategoryAchievement,
    modifier: Modifier = Modifier
) {
    // 1. Calculate progress safely (Completed divided by Total)
    val progressPercentage = if (achievement.totalLessons > 0) {
        achievement.completedLessons.toFloat() / achievement.totalLessons.toFloat()
    } else {
        0f
    }

    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp) // Spacing between the major sections
        ) {

            // --- Top Row: Icon and Title ONLY ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = achievement.iconUrl),
                        contentDescription = achievement.categoryName,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = achievement.categoryName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // --- New Centered Row: Average Score Badge ---
            Surface(
                modifier = Modifier.align(Alignment.CenterHorizontally), // <--- This centers the badge!
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), // Added slightly more padding for a better standalone look
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "Average Score",
                        modifier = Modifier.size(16.dp) // Made the icon slightly bigger to fit the new centered layout
                    )
                    Text(
                        text = "${achievement.averageScore}% Average Score",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold) // Bumped up the text size a bit
                    )
                }
            }

            // --- Middle Section: Detailed Stats (Completed, In Progress, Locked) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CategoryMiniStat(
                    label = "Completed",
                    value = achievement.completedLessons,
                    valueColor = MaterialTheme.colorScheme.primary
                )

                CategoryMiniStat(
                    label = "In Progress",
                    value = achievement.inProgressLessons,
                    valueColor = MaterialTheme.colorScheme.secondary
                )

                CategoryMiniStat(
                    label = "Not Started",
                    value = achievement.unDoneLessons,
                    valueColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }

            // --- Bottom Section: Progress Bar ---
            LinearProgressIndicator(
                progress = { progressPercentage },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer,
                strokeCap = StrokeCap.Round
            )
        }
    }
}

/**
 * A private helper component to display a single stat cleanly (Number on top, Label on bottom).
 */
@Composable
private fun CategoryMiniStat(
    label: String,
    value: Int,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = valueColor
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// ==========================================
// PREVIEW
// ==========================================

@Preview(showBackground = true, name = "Category Item - Advanced Stats")
@Composable
fun CategoryAchievementItemPreview() {
    // Mock data using the updated model parameters
    val mockAchievement = CategoryAchievement(
        categoryId = "1",
        categoryName = "Android Development",
        averageScore = 92,
        completedLessons = 10,
        inProgressLessons = 3,
        unDoneLessons = 7,
        totalLessons = 20 // 10 completed out of 20 = 50% progress bar
    )

    MaterialTheme {
        Surface(
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            CategoryAchievementItem(achievement = mockAchievement)
        }
    }
}