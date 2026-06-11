package com.example.learningapp.lessonProgress.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learningapp.lessonProgress.models.AssessmentResponse
import com.example.learningapp.lessonProgress.models.MispronouncedWord
import com.example.learningapp.lessonProgress.models.PronunciationScores
import com.example.learningapp.lessonProgress.models.Substitution

/**
 * A clean, elegant speech bubble that knows how to highlight wrong words
 * based on the Azure Pronunciation Assessment evaluation.
 */
@Composable
fun SpeechBubble(
    text: String,
    evaluation: AssessmentResponse?, // שונה מ-ASRCombinedOut?
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .animateContentSize(), // Smoothly resizes if text changes
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant, // Gentle background color
        shadowElevation = 4.dp
    ) {
        // Create the annotated text string based on evaluation results
        val styledText = buildFeedbackText(text, evaluation)

        Text(
            text = styledText,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(24.dp)
        )
    }
}

/**
 * Helper function to parse the target text and apply hierarchical colors to mistakes.
 * BEST PRACTICE: Differentiates between missing/substituted words (Hard Errors)
 * and poorly pronounced words (Soft Errors) to maximize learning value.
 *
 * @param targetText The original sentence the user was supposed to say.
 * @param evaluation The Assessment response containing the detailed Azure analysis.
 * @param errorColor Color for missing or completely wrong words (Default: Red/Error).
 * @param warningColor Color for mispronounced words (Default: Orange).
 * @return An [AnnotatedString] with the correct pedagogical styling applied.
 */
@Composable
@ReadOnlyComposable
fun buildFeedbackText(
    targetText: String,
    evaluation: AssessmentResponse?,
    errorColor: Color = MaterialTheme.colorScheme.error,
    warningColor: Color = Color(0xFFFFA500) // Elegant Orange for pronunciation tweaks
): AnnotatedString {

    // If there is no evaluation yet, just return the plain text.
    if (evaluation == null) {
        return AnnotatedString(targetText)
    }

    // 1. Gather "Hard Errors" (Missing words or expected words that were substituted)
    val hardErrors = mutableSetOf<String>()
    hardErrors.addAll(evaluation.missingWords.map { it.lowercase() })
    evaluation.substitutions.forEach { hardErrors.add(it.expected.lowercase()) }

    // 2. Gather "Soft Errors" (Words spoken, but flagged by Azure with low accuracy)
    val softErrors = mutableSetOf<String>()
    evaluation.mispronouncedWords.forEach { softErrors.add(it.word.lowercase()) }

    // Early return ONLY if both error sets are completely empty (Flawless pronunciation)
    if (hardErrors.isEmpty() && softErrors.isEmpty()) {
        return AnnotatedString(targetText)
    }

    // 3. Build the annotated string, applying the pedagogical colors
    return buildAnnotatedString {
        val words = targetText.split(" ")

        words.forEachIndexed { index, word ->
            // Clean punctuation to ensure accurate matching (e.g., "coffee," -> "coffee")
            val cleanWord = word.replace(Regex("[^A-Za-z0-9]"), "").lowercase()

            when {
                hardErrors.contains(cleanWord) -> {
                    // Apply Red + Bold for critical mistakes
                    withStyle(style = SpanStyle(color = errorColor, fontWeight = FontWeight.Bold)) {
                        append(word)
                    }
                }
                softErrors.contains(cleanWord) -> {
                    // Apply Orange + Bold for pronunciation improvements
                    withStyle(style = SpanStyle(color = warningColor, fontWeight = FontWeight.Bold)) {
                        append(word)
                    }
                }
                else -> {
                    // Correctly pronounced word
                    append(word)
                }
            }

            // Re-add the space after the word (unless it's the last word)
            if (index < words.size - 1) {
                append(" ")
            }
        }
    }
}

// ==========================================
// PREVIEWS
// ==========================================

private const val TARGET_SENTENCE = "I would like a cup of coffee, please."

// 1. Mock for a flawless pronunciation
private val mockPerfectEvaluation = AssessmentResponse(
    sentenceId = "test_123",
    runId = "run_456",
    recognizedText = "I would like a cup of coffee please",
    targetSentence = TARGET_SENTENCE,
    scores = PronunciationScores(
        accuracy = 100f,
        fluency = 100f,
        completeness = 100f,
        prosody = 100f,
        pronunciation = 100f
    ),
    words = emptyList(), // Not needed for the bubble UI
    finalScore = 100,
    isPassed = true,
    missingWords = emptyList(),
    extraWords = emptyList(),
    substitutions = emptyList(),
    mispronouncedWords = emptyList(),
    feedbackPoints = emptyList(),
    feedbackText = "Excellent pronunciation! Great job."
)

// 2. Mock for mixed errors (Hard and Soft) to test the visual hierarchy
private val mockMistakesEvaluation = AssessmentResponse(
    sentenceId = "test_124",
    runId = "run_456",
    recognizedText = "I would like a of tea pleeez",
    targetSentence = TARGET_SENTENCE,
    scores = PronunciationScores(
        accuracy = 60f,
        fluency = 75f,
        completeness = 80f,
        prosody = 70f,
        pronunciation = 65f
    ),
    words = emptyList(),
    finalScore = 65,
    isPassed = false,
    missingWords = listOf("cup"), // Hard Error -> Should be RED
    extraWords = emptyList(),
    substitutions = listOf(
        Substitution(expected = "coffee", heard = "tea") // Hard Error -> Should be RED
    ),
    mispronouncedWords = listOf(
        MispronouncedWord(
            word = "please", // Soft Error -> Should be ORANGE
            accuracyScore = 45f,
            weakPhonemes = listOf("z")
        )
    ),
    feedbackPoints = emptyList(),
    feedbackText = "You missed the word 'cup', said 'tea' instead of 'coffee', and work on your pronunciation of 'please'."
)

@Preview(showBackground = true, name = "1. Speech Bubble - Null (Waiting)")
@Composable
fun SpeechBubbleNullPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            SpeechBubble(
                text = TARGET_SENTENCE,
                evaluation = null // No evaluation yet
            )
        }
    }
}

@Preview(showBackground = true, name = "2. Speech Bubble - Perfect")
@Composable
fun SpeechBubblePerfectPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            SpeechBubble(
                text = TARGET_SENTENCE,
                evaluation = mockPerfectEvaluation // User nailed it -> Plain text
            )
        }
    }
}

@Preview(showBackground = true, name = "3. Speech Bubble - Mixed Mistakes")
@Composable
fun SpeechBubbleMistakesPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            SpeechBubble(
                text = TARGET_SENTENCE,
                evaluation = mockMistakesEvaluation
                // "cup" & "coffee" should be RED & Bold!
                // "please" should be ORANGE & Bold!
            )
        }
    }
}