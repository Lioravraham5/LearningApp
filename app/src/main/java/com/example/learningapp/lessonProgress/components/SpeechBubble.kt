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
import com.example.learningapp.lessonProgress.models.ASRCombinedOut
import com.example.learningapp.lessonProgress.models.LLMOut
import com.example.learningapp.lessonProgress.models.Substitution

/**
 * A clean, elegant speech bubble that knows how to highlight wrong words
 * based on the LLM evaluation.
 */
@Composable
fun SpeechBubble(
    text: String,
    evaluation: ASRCombinedOut?,
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
 * Helper function to parse the target text and apply colors to mistakes.
 * BEST PRACTICE: By moving the color resolution outside or passing it as a parameter,
 * this logic remains pure and highly testable.
 *
 * @param targetText The original sentence the user was supposed to say.
 * @param evaluation The LLM feedback containing substitutions and missing words.
 * @return An [AnnotatedString] with the styling applied.
 */
@Composable
@ReadOnlyComposable // Indicates this function only reads Compose state (like colors)
fun buildFeedbackText(
    targetText: String,
    evaluation: ASRCombinedOut?,
    errorColor: Color = MaterialTheme.colorScheme.error // Defaulting to the theme's error color
): AnnotatedString {

    // If no evaluation yet, or the user got it perfectly right, return plain text
    if (evaluation == null || evaluation.llm.isCorrect) {
        return AnnotatedString(targetText)
    }

    // Collect all the words the user got wrong (missing or substituted expected words)
    val errorWords = mutableSetOf<String>()

    errorWords.addAll(evaluation.llm.missingWords.map { it.lowercase() })
    evaluation.llm.substitutions.forEach { errorWords.add(it.expected.lowercase()) }

    // Build the string word by word, highlighting matches
    return buildAnnotatedString {
        // Split by spaces to evaluate word by word
        val words = targetText.split(" ")

        words.forEachIndexed { index, word ->
            // Clean punctuation for matching purposes (e.g., "apple," -> "apple")
            val cleanWord = word.replace(Regex("[^A-Za-z0-9]"), "").lowercase()

            if (errorWords.contains(cleanWord)) {
                // Apply error styling (Red + Bold) to the missed word
                withStyle(
                    style = SpanStyle(
                        color = errorColor,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(word)
                }
            } else {
                // Normal word
                append(word)
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

private val mockPerfectEvaluation = ASRCombinedOut(
    transcript = "I would like a cup of coffee please",
    llm = LLMOut(
        isCorrect = true,
        correctedText = "I would like a cup of coffee please",
        missingWords = emptyList(),
        extraWords = emptyList(),
        substitutions = emptyList(),
        feedback = listOf("Perfect pronunciation!"),
        score = 100,
        detectedLanguage = "en"
    )
)

private val mockErrorEvaluation = ASRCombinedOut(
    transcript = "I would like a of tea please",
    llm = LLMOut(
        isCorrect = false,
        correctedText = "I would like a cup of coffee please",
        missingWords = listOf("cup"), // The user missed this word entirely
        extraWords = emptyList(),
        substitutions = listOf(
            Substitution(
                expected = "coffee",
                heard = "tea"
            ) // The user said "tea" instead of "coffee"
        ),
        feedback = listOf("You missed the word 'cup' and said 'tea' instead of 'coffee'."),
        score = 75,
        detectedLanguage = "en"
    )
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
                evaluation = mockPerfectEvaluation // User nailed it
            )
        }
    }
}

@Preview(showBackground = true, name = "3. Speech Bubble - With Mistakes")
@Composable
fun SpeechBubbleMistakesPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            SpeechBubble(
                text = TARGET_SENTENCE,
                evaluation = mockErrorEvaluation // Words "cup" and "coffee" should be red & bold!
            )
        }
    }
}