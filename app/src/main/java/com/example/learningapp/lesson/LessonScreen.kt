package com.example.learningapp.lesson

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learningapp.avatar.Avatar
import com.example.learningapp.audio.AudioPlayer
import com.example.learningapp.audio.SampleAudio
import com.example.learningapp.network.AsrApi
import com.example.learningapp.ui.theme.LearningAppTheme
import org.json.JSONObject


private enum class DemoPhase { IDLE, SPEAKING, PLAYING, ANALYZING, DONE }

@Composable
fun LessonScreen(
    viewModel: LessonViewModel = viewModel()
) {
    val context = LocalContext.current
    val targetSentence = "Where is the bus stop?"

    var phase by remember { mutableStateOf(DemoPhase.IDLE) }

    // when not null -> avatar reads it aloud, then we clear it
    var feedbackToSpeak by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(feedbackToSpeak) {
        feedbackToSpeak?.let { text ->
            viewModel.speak(text)
            feedbackToSpeak = null
        }
    }

    fun runDemo() {
        phase = DemoPhase.SPEAKING
        viewModel.speak(
            "You have picked the travel category. Let's start with our first sentence. " +
                    "Repeat after me: $targetSentence",
            onDone = {
                phase = DemoPhase.PLAYING

                AudioPlayer.playSample(context) {
                    // ONLY after sample.wav finished:
                    phase = DemoPhase.ANALYZING

                    val audioFile = SampleAudio.ensureSampleWavFile(context)

                    AsrApi.sendAudioToAsr(
                        audioFile = audioFile,
                        targetSentence = targetSentence,
                        onResult = { json ->
                            phase = DemoPhase.DONE
                            feedbackToSpeak = extractFeedbackText(json)
                                ?: "I received the result, but there was no feedback."
                        },
                        onError = { err ->
                            phase = DemoPhase.DONE
                            feedbackToSpeak = "Sorry, I couldn't analyze your speech right now."
                        }
                    )
                }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Avatar(visemeId = viewModel.currentVisemeId)
        Spacer(modifier = Modifier.height(24.dp))
        Text(targetSentence)
        Spacer(modifier = Modifier.height(16.dp))

        if (phase == DemoPhase.ANALYZING) {
            Spacer(modifier = Modifier.height(16.dp))
        }

        val buttonEnabled = phase == DemoPhase.IDLE || phase == DemoPhase.DONE
        val buttonText = when (phase) {
            DemoPhase.IDLE -> "Start"
            DemoPhase.SPEAKING -> "Speaking..."
            DemoPhase.PLAYING -> "Listening..."
            DemoPhase.ANALYZING -> "Analyzing your answer..."
            DemoPhase.DONE -> "Next"
        }

        Button(
            enabled = buttonEnabled,
            onClick = {
                // For demo: Start and Next both run the demo again.
                // Later "Next" can navigate to next sentence.
                runDemo()
            }
        ) {
            Text(buttonText)
        }
    }
}

private fun extractFeedbackText(json: String): String? {
    return try {
        val obj = JSONObject(json)
        val llm = obj.optJSONObject("llm") ?: return null
        val arr = llm.optJSONArray("feedback") ?: return null
        if (arr.length() == 0) return null

        buildString {
            for (i in 0 until arr.length()) {
                val line = arr.optString(i).trim()
                if (line.isNotEmpty()) {
                    if (isNotEmpty()) append(" ")
                    append(line)
                }
            }
        }.takeIf { it.isNotBlank() }
    } catch (_: Exception) {
        null
    }
}

@Preview(showBackground = true)
@Composable
fun LessonScreenPreview() {
    LearningAppTheme { LessonScreen() }
}