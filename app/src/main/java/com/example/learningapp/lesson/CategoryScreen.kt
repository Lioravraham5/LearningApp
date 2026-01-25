package com.example.learningapp.lesson

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learningapp.avatar.Avatar
import kotlinx.coroutines.delay


@Composable
fun CategoryScreen(
    onPickTravel: () -> Unit,
    onPickJobInterview: () -> Unit,
    onPickEverydayConversation: () -> Unit,
    viewModel: LessonViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        delay(25)
        viewModel.speak(
            "Hi everyone.\n" +
                    "You’re watching a presentation, it’s been a long day, and suddenly… I start talking.\n" +
                    "I’m the AI avatar behind this project.\n" +
                    "My job is to help people practice English without fear, pressure, or public embarrassment.\n" +
                    "You can mispronounce words, repeat yourself, or pause to think, I have infinite patience and zero emotions.\n" +
                    "I listen, I analyze, and I give feedback.\n" +
                    "And the best part?\n" +
                    "If I sound smarter than you… remember, I was trained yesterday."
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Avatar(visemeId = viewModel.currentVisemeId)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onPickTravel) { Text("Travel") }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onPickJobInterview) { Text("Job Interview") }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onPickEverydayConversation) { Text("Everyday Conversation") }
    }
}