package com.example.learningapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
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
 * A reusable, Material Design 3 compliant Empty State component.
 * Used when a list-backed screen successfully loads but has no items to show.
 *
 * @param modifier Allows the caller to adjust the layout sizing/padding.
 * @param icon The primary icon displayed at the top.
 * @param title The main headline.
 * @param message The detailed description.
 */
@Composable
fun EmptyStateComponent(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.AutoMirrored.Rounded.MenuBook,
    title: String = "Nothing here yet",
    message: String = "Check back later."
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

// ==========================================
// PREVIEWS
// ==========================================

@Preview(showBackground = true, name = "Default Empty State")
@Composable
fun EmptyStateComponentPreview() {
    MaterialTheme {
        Surface {
            EmptyStateComponent()
        }
    }
}

@Preview(showBackground = true, name = "Custom Empty State")
@Composable
fun CustomEmptyStatePreview() {
    MaterialTheme {
        Surface {
            EmptyStateComponent(
                title = "No lessons yet",
                message = "Lessons for this category will appear here soon."
            )
        }
    }
}
