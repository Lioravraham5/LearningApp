package com.example.learningapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A reusable, Material Design 3 compliant Error Screen component.
 * * BEST PRACTICE: All parameters have sensible defaults, allowing it to be used
 * instantly without arguments, or fully customized per screen.
 * * @param modifier Allows the caller to adjust the layout sizing/padding.
 * @param icon The primary error icon displayed at the top.
 * @param title The main error headline.
 * @param message The detailed error description.
 * @param onRetry An optional callback. If provided, a "Try Again" button will appear.
 */
@Composable
fun ErrorStateComponent(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Rounded.ErrorOutline,
    title: String = "Oops! Something went wrong.",
    message: String = "We encountered an unexpected error. Please check your connection and try again later.",
    onRetry: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp), // Safe padding from screen edges
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Error Icon
        Icon(
            imageVector = icon,
            contentDescription = "Error Icon",
            modifier = Modifier.size(80.dp),
            // Material 3 semantic color for errors
            tint = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 2. Main Title
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 3. Detailed Message
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            // Muted text color for secondary information
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        // 4. Optional Retry Button
        if (onRetry != null) {
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onRetry) {
                Text(text = "Try Again")
            }
        }
    }
}

// ==========================================
// PREVIEWS
// ==========================================

@Preview(showBackground = true, name = "Default Error State")
@Composable
fun ErrorStateComponentPreview() {
    MaterialTheme {
        Surface {
            // Using it without any parameters (defaults applied)
            ErrorStateComponent()
        }
    }
}

@Preview(showBackground = true, name = "Custom Error with Retry")
@Composable
fun CustomErrorStatePreview() {
    MaterialTheme {
        Surface {
            // Using it with custom text and a retry button
            ErrorStateComponent(
                title = "No Internet Connection",
                message = "It looks like you are offline. Please connect to Wi-Fi and try again.",
                onRetry = { /* Do nothing in preview */ }
            )
        }
    }
}