package com.example.learningapp.lessonProgress

import com.example.learningapp.lessonProgress.models.ASRCombinedOut
import com.example.learningapp.lessonProgress.models.Sentence

/**
 * Represents the exact current step of the lesson flow.
 * This acts as a State Machine for the UI to know what to display and how to react.
 */
enum class LessonStep {
    LOADING_SENTENCES,    // Fetching the list of sentences from the server
    READY_TO_START,       // Avatar is idle, waiting for the user to press "Play"
    AVATAR_SPEAKING,      // Avatar is reading the sentence (TTS is active)
    WAITING_FOR_RECORDING,// Waiting for the user to tap the mic to speak
    RECORDING,            // The microphone is open and recording the user
    ANALYZING,            // Audio sent to FastAPI, showing a loader waiting for LLMOut
    SHOWING_FEEDBACK,     // Displaying the score and highlighting wrong words
    LESSON_COMPLETED      // All sentences are done, showing the final summary
}

/**
 * The single source of truth for the Lesson Progress screen's UI.
 * The Compose UI will only read from this data class.
 */
data class LessonProgressState(
    val step: LessonStep = LessonStep.LOADING_SENTENCES,
    val sentences: List<Sentence> = emptyList(),
    val currentSentenceIndex: Int = 0,
    // Azure TTS Viseme ID for the Avatar's mouth
    val currentVisemeId: Int = 0,
    // The evaluation result received from the server for the CURRENT sentence
    val currentEvaluation: ASRCombinedOut? = null,
    // General error message for network/hardware failures
    val errorMessage: String? = null
) {

    /**
     * Helper property to easily access the active sentence.
     * Best Practice: Keeps the Compose UI clean from index out-of-bounds checks.
     */
    val currentSentence: Sentence?
        get() = sentences.getOrNull(currentSentenceIndex)

    /**
     * Helper property to calculate the progress bar state (0.0f to 1.0f).
     */
    val progressPercentage: Float
        get() = if (sentences.isNotEmpty()) {
            currentSentenceIndex.toFloat() / sentences.size
        } else 0f
}