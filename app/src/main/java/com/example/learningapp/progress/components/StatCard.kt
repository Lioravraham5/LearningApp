package com.example.learningapp.progress.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    contentColor: Color,
    modifier: Modifier = Modifier,
    // Background color with a default value
    containerColor: Color = MaterialTheme.colorScheme.surface,
    description: String = ""
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = containerColor
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp) // Slightly more space for the new layout
        ) {
            // Row containing Icon and Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Icon Container
                Box(
                    modifier = Modifier
                        .size(36.dp) // Slightly smaller to fit better in a Row
                        .background(contentColor.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Stat Title (Placed next to the icon)
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = contentColor,
                    maxLines = 1 // Keeps the UI clean if the title is long
                )
            }

            // Stat Value (Large number below the icon/title row)
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = contentColor,
                maxLines = 1
            )

            // Stat Value (Large number below the icon/title row)
            Text(
                text = description,
                style = MaterialTheme.typography.titleSmall,
                color = contentColor,
                maxLines = 1
            )


        }
    }
}

@Preview(showBackground = true, name = "Stat Cards with Custom Colors")
@Composable
fun StatCardCustomColorPreview() {
    MaterialTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            // Using a Column to show different layout possibilities
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                // Row with standard surface cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatCard(
                        title = "Streak",
                        value = "15",
                        icon = Icons.Default.LocalFireDepartment,
                        contentColor = Color(0xFFFF5722),
                        modifier = Modifier.weight(1f),
                        description = "Days in a row"

                    )

                    StatCard(
                        title = "Score",
                        value = "94%",
                        icon = Icons.Default.Star,
                        contentColor = Color(0xFFFFC107),
                        modifier = Modifier.weight(1f),
                        description = "Average score"
                    )
                }

                // A special highlighted card for Badges
                StatCard(
                    title = "Total Earned Badges",
                    value = "12",
                    icon = Icons.Default.EmojiEvents,
                    contentColor = Color(0xFF0D47A1),
                    // Custom background color (Golden/Amber)
                    containerColor = Color(0xFFBBDEFB),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}