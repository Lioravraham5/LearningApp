package com.example.learningapp.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
    // We use a Row to align the monogram, the text column, and the action button horizontally.
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // 1. User Monogram (Circle with the first letter of the name)
        // Extract the first character safely, fallback to "S" if the name is empty.
        val initialLetter = userName.firstOrNull()?.uppercase() ?: "S"

        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                // Using M3 semantic colors: PrimaryContainer is perfect for soft avatar backgrounds
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initialLetter,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 2. User Details (Name & Email)
        Column(
            modifier = Modifier.weight(1f) // Takes up all available space, pushing the icon to the end
        ) {
            Text(
                text = userName,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )

            Text(
                text = userEmail,
                style = MaterialTheme.typography.bodyMedium,
                // onSurfaceVariant is the M3 standard for secondary/subdued text
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }

        // 3. Edit Action
        IconButton(onClick = onEditClick) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Profile Name",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// Preview function to test the UI independently
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