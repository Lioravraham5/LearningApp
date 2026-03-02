package com.example.learningapp.avatar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learningapp.R
import com.example.learningapp.core.AvatarType

/**
 * Global Avatar component that reacts to the selected gender and current viseme (mouth shape).
 * Uses BoxWithConstraints to scale the mouth perfectly regardless of the parent size.
 *
 * @param modifier Modifier for external layout adjustments (e.g., size).
 * @param avatarType Determines the base character image.
 * @param visemeId Determines the current mouth shape.
 */
@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    avatarType: AvatarType = AvatarType.MALE,
    visemeId: Int
) {
    val avatarFigure = when (avatarType) {
        AvatarType.MALE -> R.drawable.avatar_base_male
        AvatarType.FEMALE -> R.drawable.avatar_base_female
    }

    // BoxWithConstraints allows us to read maxWidth and maxHeight dynamically
    BoxWithConstraints(
        // The aspectRatio(1f) ensures the avatar remains a perfect square, preventing distortion
        modifier = modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        // 1. Base Avatar
        Image(
            painter = painterResource(id = avatarFigure),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Calculate proportional dimensions based on the original 300dp design
        // Size: 60dp / 300dp = 0.2 (20% of the total width)
        val proportionalMouthSize = maxWidth * 0.2f

        // Offset X: -3dp / 300dp = -0.01 (-1% of the total width)
        val proportionalOffsetX = maxWidth * (-0.01f)

        // Offset Y: 65dp / 300dp = 0.2166 (approx 21.6% of the total height)
        val proportionalOffsetY = maxHeight * (65f / 300f)

        // 3. Mouth Avatar
        Image(
            painter = painterResource(id = getVisemeDrawable(visemeId)),
            contentDescription = null,
            modifier = Modifier
                .size(proportionalMouthSize) // Dynamic size
                .absoluteOffset(x = proportionalOffsetX, y = proportionalOffsetY) // Dynamic position
        )
    }
}

// Preview showing both a Large and a Small avatar to prove the scaling works perfectly
@Preview(showBackground = true)
@Composable
fun AvatarPreview() {
    androidx.compose.foundation.layout.Column(
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Large version (Original size)
        Avatar(
            modifier = Modifier.size(300.dp),
            avatarType = AvatarType.MALE,
            visemeId = 1
        )

        // Small version (Used in the Selector Card)
        Avatar(
            modifier = Modifier.size(80.dp),
            avatarType = AvatarType.FEMALE,
            visemeId = 7
        )
    }
}