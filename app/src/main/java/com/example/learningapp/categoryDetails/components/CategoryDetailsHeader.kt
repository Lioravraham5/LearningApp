package com.example.learningapp.categoryDetails.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.learningapp.R
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Best Practice: The "if" condition was removed so AsyncImage can always render.
        // If the URL is null, it will gracefully fall back to the placeholder image.
        AsyncImage(
            model = category.iconUrl,
            contentDescription = category.title,
            modifier = Modifier.size(96.dp),
            contentScale = ContentScale.Fit,
            placeholder = painterResource(id = R.drawable.category_icon_loading_fallback), // On loading image
            error = painterResource(id = R.drawable.category_icon_error_fallback),       // On loading image failed
            fallback = painterResource(id = R.drawable.category_icon_error_fallback)     // On null icon
        )

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

// ==========================================
// PREVIEW
// ==========================================

@Preview(showBackground = true, name = "Category Details Header - With Fallback")
@Composable
fun CategoryDetailsHeaderPreview() {
    // 1. Create mock data for the category
    val mockCategory = CategoryDetails(
        id = "cat_1",
        title = "Advanced Grammar",
        description = "Master the complexities of verb conjugations, sentence structures, and advanced tenses to speak like a native.",
        iconUrl = null, // Coil will safely catch this null and display the fallback placeholder!
        lessons = emptyList()
    )

    MaterialTheme {
        // 2. Wrap in a Surface to ensure correct background and text colors
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            CategoryDetailsHeader(
                category = mockCategory,
                modifier = Modifier.padding(vertical = 32.dp)
            )
        }
    }
}