package com.example.learningapp.lessonProgress.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.learningapp.avatar.Avatar
import com.example.learningapp.avatar.AvatarType
import com.example.learningapp.lessonProgress.models.AssessmentResponse
import com.example.learningapp.lessonProgress.models.MispronouncedWord
import com.example.learningapp.lessonProgress.models.PronunciationScores
import com.example.learningapp.lessonProgress.models.Substitution

/**
 * Groups the Avatar and the Speech Bubble together.
 * By passing only the necessary data down (sentence, evaluation, visemeId), we keep this component completely stateless and highly testable.
 */
@Composable
fun AvatarSpeechSection(
    modifier: Modifier = Modifier,
    targetSentence: String?,
    evaluation: AssessmentResponse?, // שונה מ-ASRCombinedOut?
    visemeId: Int,
    avatarType: AvatarType = AvatarType.MALE // Assuming AvatarType is accessible here
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 1. Avatar is now on top
        Avatar(
            modifier = Modifier.size(250.dp),
            avatarType = avatarType,
            visemeId = visemeId
        )

        // 2. Speech bubble and its arrow are below the Avatar
        if (targetSentence != null) {
            // The tiny arrow pointing UP from the bubble to the avatar
            Icon(
                imageVector = Icons.Filled.ArrowDropUp,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .size(36.dp)
                    .offset(y = 16.dp) // Push it down slightly to attach seamlessly to the top of the bubble
                    .zIndex(1f) // BEST PRACTICE: Ensure the arrow renders above the bubble, hiding the gap
            )

            SpeechBubble(
                text = targetSentence,
                evaluation = evaluation
            )
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
    recognizedText = "I would like a cup of coffee please",
    targetSentence = TARGET_SENTENCE,
    scores = PronunciationScores(
        accuracy = 100f,
        fluency = 100f,
        completeness = 100f,
        prosody = 100f,
        pronunciation = 100f
    ),
    words = emptyList(),
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
    missingWords = listOf("cup"), // Hard Error -> RED
    extraWords = emptyList(),
    substitutions = listOf(
        Substitution(expected = "coffee", heard = "tea") // Hard Error -> RED
    ),
    mispronouncedWords = listOf(
        MispronouncedWord(
            word = "please", // Soft Error -> ORANGE
            accuracyScore = 45f,
            weakPhonemes = listOf("z")
        )
    ),
    feedbackPoints = emptyList(),
    feedbackText = "You missed the word 'cup', said 'tea' instead of 'coffee', and work on your pronunciation of 'please'."
)

@Preview(showBackground = true, name = "1. Avatar Section - Waiting (No Feedback)")
@Composable
fun AvatarSpeechSectionWaitingPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            AvatarSpeechSection(
                targetSentence = TARGET_SENTENCE,
                evaluation = null,
                visemeId = 0, // Default closed mouth
                avatarType = AvatarType.MALE
            )
        }
    }
}

@Preview(showBackground = true, name = "2. Avatar Section - Perfect!")
@Composable
fun AvatarSpeechSectionPerfectPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            AvatarSpeechSection(
                targetSentence = TARGET_SENTENCE,
                evaluation = mockPerfectEvaluation,
                visemeId = 1, // Smiling/Open mouth
                avatarType = AvatarType.FEMALE
            )
        }
    }
}

@Preview(showBackground = true, name = "3. Avatar Section - With Mistakes")
@Composable
fun AvatarSpeechSectionMistakesPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            AvatarSpeechSection(
                targetSentence = TARGET_SENTENCE,
                evaluation = mockMistakesEvaluation, // Using the new structured errors
                visemeId = 4, // Another mouth shape (talking)
                avatarType = AvatarType.MALE
            )
        }
    }
}

@Preview(showBackground = true, name = "4. Avatar Section - Hidden Bubble (Null Sentence)")
@Composable
fun AvatarSpeechSectionHiddenBubblePreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            AvatarSpeechSection(
                targetSentence = null, // This should hide the speech bubble completely
                evaluation = null,
                visemeId = 0,
                avatarType = AvatarType.FEMALE
            )
        }
    }
}