package com.example.learningapp.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A simple header component to greet the user.
 *
 * @param userName The name of the currently logged-in user.
 * @param modifier Modifier for external styling or layout adjustments.
 */
@Composable
fun HomeHeader(
    userName: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        // Large bold user name
        Text(
            text = "Hello, $userName! \uD83D\uDC4B", // Adds a waving hand emoji
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Small greeting text
        Text(
            text = "Ready to practice today?",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// ==========================================
// PREVIEW
// ==========================================

@Preview(showBackground = true)
@Composable
fun HomeHeaderPreview() {
    MaterialTheme {
        HomeHeader(userName = "Student Name")
    }
}