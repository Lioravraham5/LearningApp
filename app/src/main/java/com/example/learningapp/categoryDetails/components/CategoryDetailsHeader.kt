package com.example.learningapp.categoryDetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learningapp.categoryDetails.CategoryDetails

/**
 * A private helper component specifically for the beautiful top section of the details screen.
 */
@Composable
fun CategoryDetailsHeader(
    category: CategoryDetails,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally, // Center everything
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Large Circular Icon Background
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = category.iconRes),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(48.dp)
            )
        }

        // Category Title
        Text(
            text = category.title,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        // Category Description
        Text(
            text = category.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview(showBackground = true, name = "Category Details Header")
@Composable
fun CategoryDetailsHeaderPreview() {
    // 1. Create mock data for the category
    val mockCategory = CategoryDetails(
        id = "cat_1",
        title = "Advanced Grammar",
        description = "Master the complexities of verb conjugations, sentence structures, and advanced tenses to speak like a native.",
        iconRes = android.R.drawable.ic_menu_sort_alphabetically, // Safe vector icon for preview
        lessons = emptyList() // The header doesn't render lessons, so an empty list is fine here
    )

    MaterialTheme {
        // 2. Wrap in a Surface to ensure correct background and text colors
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            CategoryDetailsHeader(
                category = mockCategory,
                // Add some vertical padding just so it looks nice and centered in the preview window
                modifier = Modifier.padding(vertical = 32.dp)
            )
        }
    }
}