package com.example.learningapp.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview

/**
 * Displays the user's personal identity information (Name, Email, and a Monogram avatar).
 * This is a stateless component, adhering to Unidirectional Data Flow (UDF) best practices.
 *
 * @param userName The display name of the user.
 * @param userEmail The email address of the user.
 * @param onEditClick Callback triggered when the edit icon is pressed.
 * @param modifier Modifier for external layout adjustments.
 */
@Composable
fun UserIdentitySection(
    userName: String,
    userEmail: String,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Outer Column to stack the Avatar ON TOP of the details row
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp), // General vertical padding
        horizontalAlignment = Alignment.CenterHorizontally // Centers the Avatar
    ) {

        // 1. Large Modern Monogram (Centered at the top)
        val initialLetter = userName.firstOrNull()?.uppercase() ?: "S"

        Box(
            modifier = Modifier
                .size(100.dp) // Large modern size
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initialLetter,
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.height(24.dp)) // Nice breathing room between avatar and text

        // 2. User Details & Edit Action (The classic Row layout you liked)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), // Horizontal padding for the row
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Text Column (Pushes the edit button to the end)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = userName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = userEmail,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Edit Action (Anchored to the right)
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Profile Name",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// Preview function
@Preview(showBackground = true)
@Composable
fun UserIdentitySectionPreview() {
    MaterialTheme {
        UserIdentitySection(
            userName = "Lior Avraham",
            userEmail = "student@example.com",
            onEditClick = {}
        )
    }
}