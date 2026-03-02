package com.example.learningapp.profile.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

/**
 * A destructive action dialog warning the user before account deletion.
 * Follows M3 guidelines for critical warnings (centered icon, error colored action).
 *
 * @param onDismiss Callback to close the dialog.
 * @param onConfirm Callback to proceed with account deletion.
 */
@Composable
fun DeleteAccountDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        // Adding an icon above the title is a great M3 pattern for warnings
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Warning",
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text(text = "Delete Account?")
        },
        text = {
            Text(
                text = "Are you sure you want to permanently delete your account? All your learning progress will be lost. This action cannot be revert.",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            // TextButton for destructive actions is standard, colored with the Error color
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Delete", style = MaterialTheme.typography.labelLarge)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DeleteAccountDialogPreview() {
    MaterialTheme {
        DeleteAccountDialog(
            onDismiss = {},
            onConfirm = {}
        )
    }
}