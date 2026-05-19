package com.example.learningapp.lessonProgress.components

import androidx.compose.material.icons.Icons
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.ui.tooling.preview.Preview

/**
 * Dialog shown when the user attempts to exit an active lesson.
 * Confirms that they want to pause, letting them know progress will be saved.
 */
@Composable
fun ExitLessonDialog(
    onDismiss: () -> Unit,
    onConfirmExit: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                // AutoMirrored ensures the icon flips correctly for RTL languages (like Hebrew/Arabic)
                imageVector = Icons.AutoMirrored.Rounded.ExitToApp,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                text = "Pause Lesson ?",
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = "Are you sure you want to stop now? Your progress will be saved and you can resume from this sentence later.",
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmExit) {
                // Removed the 'error' color since this is no longer a destructive action
                Text("Exit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Stay",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}

// ==========================================
// PREVIEWS
// ==========================================

@Preview(name = "Exit Lesson Dialog", showBackground = true)
@Composable
fun ExitLessonDialogPreview() {
    MaterialTheme {
        ExitLessonDialog(
            onDismiss = { /* Preview: Do nothing */ },
            onConfirmExit = { /* Preview: Do nothing */ }
        )
    }
}