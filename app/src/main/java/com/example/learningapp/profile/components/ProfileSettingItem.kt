package com.example.learningapp.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A modern, premium-looking setting item.
 * Features a soft-colored icon container, rounded corners, and a trailing arrow.
 *
 * @param title The text to display.
 * @param icon The leading icon representing the action.
 * @param onClick Action to perform when the item is tapped.
 * @param modifier Modifier for external layout adjustments.
 * @param isDestructive If true, colors the item with the theme's error colors.
 */
@Composable
fun ProfileSettingItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDestructive: Boolean = false
) {
    // 1. Semantic Color Definitions
    // The text color (Red if destructive, standard text color otherwise)
    val textColor = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface

    // The background "bubble" for the icon (Soft red for error, soft primary/secondary for normal)
    val iconContainerColor = if (isDestructive) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.secondaryContainer

    // The icon itself (Dark red for error, dark primary/secondary for normal)
    val iconTint = if (isDestructive) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSecondaryContainer

    // 2. Main Row Layout (Acts as the clickable surface)
    Row(
        modifier = modifier
            .fillMaxWidth()
            // Rounded corners for the ripple effect and shape
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 16.dp), // Inner padding
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 3. Icon inside a colored bubble
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(iconContainerColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null, // Handled by the row/text
                tint = iconTint
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 4. Title Text
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = textColor,
            modifier = Modifier.weight(1f) // Pushes the trailing icon to the end
        )

        // 5. Trailing Arrow (Only show if it's NOT a destructive action like Delete)
        if (!isDestructive) {
            Icon(
                // We use AutoMirrored so it flips automatically in RTL languages (Hebrew/Arabic)
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Preview
@Preview(showBackground = true)
@Composable
fun ProfileSettingsListPreview() {
    MaterialTheme {
        androidx.compose.foundation.layout.Column(
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            ProfileSettingItem(
                title = "Notification Settings",
                icon = Icons.Default.Notifications,
                onClick = {}
            )

            ProfileSettingItem(
                title = "Logout",
                icon = Icons.AutoMirrored.Filled.Logout,
                onClick = {}
            )

            ProfileSettingItem(
                title = "Delete Account",
                icon = Icons.Default.Delete,
                isDestructive = true, // Shows error colors, hides the arrow
                onClick = {}
            )
        }
    }
}