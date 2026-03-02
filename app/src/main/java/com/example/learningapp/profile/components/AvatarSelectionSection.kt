package com.example.learningapp.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learningapp.avatar.Avatar
import com.example.learningapp.core.AvatarType

/**
 * A section displaying the title and the available tutor options side-by-side.
 *
 * @param selectedAvatar The currently selected AvatarType from the ViewModel.
 * @param onAvatarSelected Callback triggered when a new tutor is selected.
 * @param modifier Modifier for external layout adjustments.
 */
@Composable
fun AvatarSelectionSection(
    selectedAvatar: AvatarType,
    onAvatarSelected: (AvatarType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Section Title
        Text(
            text = "Your Tutor",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Row containing the choice cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Option 1: Male
            AvatarSelectorCard(
                avatarType = AvatarType.MALE,
                label = "Male",
                isSelected = selectedAvatar == AvatarType.MALE,
                onClick = { onAvatarSelected(AvatarType.MALE) },
                modifier = Modifier.weight(1f) // Ensures both cards take exactly 50% width
            )

            // Option 2: Female
            AvatarSelectorCard(
                avatarType = AvatarType.FEMALE,
                label = "Female",
                isSelected = selectedAvatar == AvatarType.FEMALE,
                onClick = { onAvatarSelected(AvatarType.FEMALE) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * A selectable card representing a single tutor option.
 * Implements Material 3 best practices for selection feedback (dynamic border, color, and icon).
 *
 * @param avatarType The gender/type of the avatar to display.
 * @param label The text displayed below the avatar.
 * @param isSelected True if this card is currently the active choice.
 * @param onClick Action to perform when the card is tapped.
 */
@Composable
fun AvatarSelectorCard(
    avatarType: AvatarType,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Determine dynamic colors based on selection state
    val containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
    val borderWidth = if (isSelected) 2.dp else 1.dp

    OutlinedCard(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.outlinedCardColors(containerColor = containerColor),
        border = BorderStroke(borderWidth, borderColor)
    ) {
        // Box is used to overlap the check icon on top of the column
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {

            // 1. Selection Indicator (Check Icon) - Top End
            if (isSelected) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }

            // 2. Avatar Preview & Label - Centered
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Scaled down avatar for preview
                Avatar(
                    avatarType = avatarType,
                    visemeId = 0, // Default neutral face
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

// Preview to test both selected and unselected states
@Preview(showBackground = true)
@Composable
fun AvatarSelectionSectionPreview() {
    MaterialTheme {
        AvatarSelectionSection(
            selectedAvatar = AvatarType.MALE,
            onAvatarSelected = {}
        )
    }
}