package com.example.learningapp.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

/**
 * A generic, reusable setting item built on top of the Material 3 ListItem component.
 * Follows M3 guidelines for settings rows.
 *
 * @param title The text to display.
 * @param icon The leading icon representing the action.
 * @param onClick Action to perform when the item is tapped.
 * @param modifier Modifier for external layout adjustments.
 * @param isDestructive If true, colors the text and icon with the theme's error color.
 */
@Composable
fun ProfileSettingItemBasic(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDestructive: Boolean = false
) {
    // 1. Determine colors based on whether this is a destructive action
    val contentColor = if (isDestructive) {
        MaterialTheme.colorScheme.error // Red (or theme error color) for deletion
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val iconColor = if (isDestructive) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant // Standard M3 color for leading icons
    }

    // 2. The standard M3 ListItem
    ListItem(
        headlineContent = {
            Text(
                text = title,
                color = contentColor,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor
            )
        },
        // We make the entire row clickable
        modifier = modifier.clickable { onClick() },
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent // Ensures it blends well with the parent screen background
        )
    )
}

// Preview showing all 3 items to demonstrate how they look together in the list
@Preview(showBackground = true)
@Composable
fun ProfileSettingsListBasicPreview() {
    MaterialTheme {
        Column {
            ProfileSettingItemBasic(
                title = "Notification Settings",
                icon = Icons.Default.Notifications,
                onClick = {}
            )

            ProfileSettingItemBasic(
                title = "Logout",
                icon = Icons.AutoMirrored.Filled.Logout,
                onClick = {}
            )

            ProfileSettingItemBasic(
                title = "Delete Account",
                icon = Icons.Default.Delete,
                isDestructive = true, // Turns the item Red
                onClick = {}
            )
        }
    }
}