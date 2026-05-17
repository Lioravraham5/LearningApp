package com.example.learningapp.lessonProgress.models

import com.google.gson.annotations.SerializedName

/**
 * Represents a replaced word where the user said something different than the target sentence.
 */
data class Substitution(
    val expected: String,
    val heard: String
)

/**
 * The evaluation results returned by the LLM (GPT-4) detailing the user's pronunciation accuracy.
 */
data class LLMOut(
    @SerializedName("is_correct") val isCorrect: Boolean,
    @SerializedName("corrected_text") val correctedText: String,
    @SerializedName("missing_words") val missingWords: List<String>,
    @SerializedName("extra_words") val extraWords: List<String>,
    val substitutions: List<Substitution>,
    val feedback: List<String>,
    @SerializedName("score_0_to_100") val score: Int,
    @SerializedName("detected_language") val detectedLanguage: String?
)

/**
 * The root response model from the /asr endpoint.
 * Contains both the raw whisper transcript and the detailed LLM evaluation.
 */
data class ASRCombinedOut(
    val transcript: String,
    val llm: LLMOut
)