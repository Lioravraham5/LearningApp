package com.example.learningapp.progress.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learningapp.progress.Badge

@Composable
fun BadgeItem(
    badge: Badge,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 1. Define visual states based on the badge status
    val isUnlocked = badge.isAchieved

    // Configure card colors: Surface for unlocked, faint surfaceVariant for locked
    val cardColors = if (isUnlocked) {
        CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    } else {
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
    }

    // Icon background: Brand color for unlocked, neutral gray for locked
    val iconBackgroundColor = if (isUnlocked) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    }

    val iconTintColor = if (isUnlocked) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
    }

    // NEW: Define a modern border for the unlocked state
    val cardBorder = if (isUnlocked) {
        BorderStroke(
            width = 1.5.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        )
    } else {
        null // No border when locked
    }

    // 2. Select card type: Elevated for achieved milestones, Flat for locked ones
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = cardColors,
        elevation = if (isUnlocked) CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
        else CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = cardBorder // <-- The border is applied here!
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // -- Icon Area --
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(72.dp)
            ) {
                // Circular icon container
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(iconBackgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = badge.iconUrl),
                        contentDescription = badge.title,
                        tint = iconTintColor,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // If locked - Add a small lock indicator at the bottom-end corner
                if (!isUnlocked) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                            .padding(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Lock,
                            contentDescription = "Locked",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // -- Text Area --
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                // Reduce text prominence globally if the badge is locked
                modifier = Modifier.alpha(if (isUnlocked) 1f else 0.6f)
            ) {
                Text(
                    text = badge.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = badge.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Badge States Comparison")
@Composable
fun BadgeItemPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Unlocked/Achieved Badge
                BadgeItem(
                    badge = Badge(
                        id = "1",
                        title = "Fast Learner",
                        description = "Complete 5 lessons in one day",
                        isAchieved = true
                    ),
                    modifier = Modifier.weight(1f),
                    onClick = { /* Do nothing */ }
                )

                // 2. Locked Badge
                BadgeItem(
                    badge = Badge(
                        id = "2",
                        title = "Polyglot",
                        description = "Learn 100 new words in a week",
                        isAchieved = false
                    ),
                    modifier = Modifier.weight(1f),
                    onClick = { /* Do nothing */ }
                )
            }
        }
    }
}