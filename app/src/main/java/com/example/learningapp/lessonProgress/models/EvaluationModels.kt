package com.example.learningapp.lessonProgress.models

import com.google.gson.annotations.SerializedName

/**
 * A single phoneme and how accurately it was pronounced (0-100).
 */
data class PhonemeScore(
    val phoneme: String,
    @SerializedName("accuracy_score") val accuracyScore: Float
)

/**
 * Per-word result from Azure's scripted Pronunciation Assessment.
 */
data class WordResult(
    val word: String,
    @SerializedName("accuracy_score") val accuracyScore: Float,
    // Azure ErrorType: "None" | "Mispronunciation" | "Omission" | "Insertion"
    @SerializedName("error_type") val errorType: String,
    val phonemes: List<PhonemeScore> = emptyList()
)

/**
 * Sentence-level scores returned by Azure (scripted mode), each 0-100.
 */
data class PronunciationScores(
    val accuracy: Float,
    val fluency: Float,
    val completeness: Float,
    val prosody: Float,
    val pronunciation: Float // Azure's own overall aggregate
)

/**
 * Represents a replaced word where the user said something different than the target sentence.
 */
data class Substitution(
    val expected: String,
    val heard: String
)

/**
 * Words Azure flagged as mispronounced (or that scored low overall).
 */
data class MispronouncedWord(
    val word: String,
    @SerializedName("accuracy_score") val accuracyScore: Float,
    // The specific phonemes inside the word that scored poorly.
    @SerializedName("weak_phonemes") val weakPhonemes: List<String> = emptyList()
)

/**
 * One prioritized thing the feedback should talk about.
 * Kinds: praise | missing_word | extra_word | substitution | mispronunciation | fluency | pattern
 */
data class FeedbackPoint(
    val kind: String,
    val priority: Int,
    val word: String? = null,
    val detail: String? = null
)

/**
 * The root response model from the /asr endpoint.
 * Contains both the raw Azure scores and the deterministic feedback analysis.
 */
data class AssessmentResponse(
    @SerializedName("sentence_id") val sentenceId: String,
    @SerializedName("recognized_text") val recognizedText: String,
    @SerializedName("target_sentence") val targetSentence: String,
    val scores: PronunciationScores,
    val words: List<WordResult>,

    @SerializedName("final_score") val finalScore: Int,
    @SerializedName("is_passed") val isPassed: Boolean,

    @SerializedName("missing_words") val missingWords: List<String>,
    @SerializedName("extra_words") val extraWords: List<String>,
    val substitutions: List<Substitution>,
    @SerializedName("mispronounced_words") val mispronouncedWords: List<MispronouncedWord>,

    @SerializedName("feedback_points") val feedbackPoints: List<FeedbackPoint>,
    // The paragraph the avatar reads aloud.
    @SerializedName("feedback_text") val feedbackText: String
)