package com.example.learningapp.progress.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.learningapp.progress.Badge

@Composable
fun BadgeDetailDialog(
    badge: Badge,
    onDismissRequest: () -> Unit
) {
    val isUnlocked = badge.isAchieved

    // Reuse the same color logic to maintain visual consistency across the app
    val containerColor = if (isUnlocked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
    val iconBackgroundColor = if (isUnlocked) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    val iconTintColor = if (isUnlocked) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)

    // Add a subtle border for unlocked badges to make them feel special
    val cardBorder = if (isUnlocked) {
        BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
    } else {
        null
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            // Increased corner radius (24.dp) for a more modern "Dialog" look
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = containerColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            border = cardBorder
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // -- Large Icon Display Area --
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(100.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .clip(CircleShape)
                            .background(iconBackgroundColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = badge.iconUrl),
                            contentDescription = badge.title,
                            tint = iconTintColor,
                            modifier = Modifier.size(48.dp) // Significantly larger icon for the detail view
                        )
                    }

                    // Floating lock indicator for locked state
                    if (!isUnlocked) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                                .padding(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Lock,
                                contentDescription = "Locked",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                // -- Text Content Area --
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    // Slightly faded appearance for locked badges
                    modifier = Modifier.alpha(if (isUnlocked) 1f else 0.7f)
                ) {
                    Text(
                        text = badge.title,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = badge.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                        // maxLines removed here to ensure the full description is readable
                    )
                }

                // -- Action Button --
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onDismissRequest,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isUnlocked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    )
                ) {
                    // Context-aware button text
                    Text(text = if (isUnlocked) "Awesome !" else "Got it")
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Badge Detail Dialog - Unlocked")
@Composable
fun BadgeDetailDialogPreview() {
    // Mock data for an impressive achieved badge
    val mockBadge = Badge(
        id = "1",
        title = "Language Master",
        description = "You have shown incredible dedication by completing 50 lessons with a perfect score! Keep up the amazing work.",
        isAchieved = true
    )

    MaterialTheme {
        // In a preview, we wrap the dialog in a Box to visualize it correctly
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.3f)), // Simulates the dimmed background
            contentAlignment = Alignment.Center
        ) {
            BadgeDetailDialog(
                badge = mockBadge,
                onDismissRequest = { /* Preview: Do nothing */ }
            )
        }
    }
}

@Preview(showBackground = true, name = "Badge Detail Dialog - Locked")
@Composable
fun BadgeDetailDialogLockedPreview() {
    // Mock data for a locked badge
    val mockBadge = Badge(
        id = "2",
        title = "Early Bird",
        description = "Complete a lesson before 7:00 AM to unlock this special achievement.",
        isAchieved = false
    )

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            BadgeDetailDialog(
                badge = mockBadge,
                onDismissRequest = { /* Preview: Do nothing */ }
            )
        }
    }
}