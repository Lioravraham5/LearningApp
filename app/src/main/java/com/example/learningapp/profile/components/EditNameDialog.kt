package com.example.learningapp.profile.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

/**
 * A modern M3 dialog for editing the user's display name.
 * Manages its own input state until the user confirms the change.
 *
 * @param currentName The user's current name to pre-fill the text field.
 * @param onDismiss Callback to close the dialog without saving.
 * @param onConfirm Callback with the new name when the user clicks save.
 */
@Composable
fun EditNameDialog(
    currentName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    // Local state to hold the text input.
    // Initialized with the current name so the user can just edit it.
    var text by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Edit Name", style = MaterialTheme.typography.headlineSmall)
        },
        text = {
            // M3 OutlinedTextField is the standard for form inputs inside dialogs
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Display Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        // The main action button (Save)
        confirmButton = {
            Button(
                // Disable the save button if the text is empty or unchanged
                enabled = text.isNotBlank() && text != currentName,
                onClick = { onConfirm(text.trim()) }
            ) {
                Text("Save")
            }
        },
        // The secondary action button (Cancel)
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        // M3 automatically applies nice rounded corners and surface colors
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface
    )
}

@Preview(showBackground = true)
@Composable
fun EditNameDialogPreview() {
    MaterialTheme {
        EditNameDialog(
            currentName = "Student Name",
            onDismiss = {},
            onConfirm = {}
        )
    }
}