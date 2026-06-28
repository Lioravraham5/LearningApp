package com.example.learningapp.lessonEnd.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ActionButtons(
    onContinueClick: () -> Unit,
    onPracticeAgainClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Primary Button
        Button(
            onClick = onContinueClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Continue",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Secondary Button
        OutlinedButton(
            onClick = onPracticeAgainClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Practice Again",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ==========================================
// PREVIEW
// ==========================================

@Preview(showBackground = true, name = "Action Buttons Preview")
@Composable
fun ActionButtonsPreview() {
    MaterialTheme {
        ActionButtons(
            onContinueClick = {},
            onPracticeAgainClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}