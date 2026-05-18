package com.example.learningapp.lessonProgress.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

/**
 * Dialog shown when the user has previously denied the microphone permission.
 */
@Composable
fun PermissionRationaleDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Rounded.Mic,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                text = "Microphone Access Required",
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = "To evaluate your pronunciation and give you feedback, the app needs access to your microphone. Please allow access on the next prompt.",
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Allow")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Not Now", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}

/**
 * Dialog shown when the user has permanently denied the permission ("Don't ask again").
 */
@Composable
fun PermissionSettingsDialog(
    onDismiss: () -> Unit,
    onOpenSettings: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Rounded.Settings,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text(
                text = "Permission Blocked",
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = "Microphone access has been permanently denied. To record your voice, please open the app settings and grant the microphone permission.",
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            TextButton(onClick = onOpenSettings) {
                Text("Open Settings")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}

// ==========================================
// PREVIEWS
// ==========================================

@Preview(name = "1. Rationale Dialog", showBackground = true)
@Composable
fun PermissionRationaleDialogPreview() {
    MaterialTheme {
        PermissionRationaleDialog(
            onDismiss = { /* Preview: Do nothing */ },
            onConfirm = { /* Preview: Do nothing */ }
        )
    }
}

@Preview(name = "2. Settings Dialog", showBackground = true)
@Composable
fun PermissionSettingsDialogPreview() {
    MaterialTheme {
        PermissionSettingsDialog(
            onDismiss = { /* Preview: Do nothing */ },
            onOpenSettings = { /* Preview: Do nothing */ }
        )
    }
}