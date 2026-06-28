package com.example.learningapp.lessonEnd.models

import com.google.gson.annotations.SerializedName

/**
 * Represents the completion status of a lesson, mirroring the backend enum.
 */
enum class ProgressStatus {
    @SerializedName("NOT_STARTED") NOT_STARTED,
    @SerializedName("IN_PROGRESS") IN_PROGRESS,
    @SerializedName("COMPLETED") COMPLETED
}

/**
 * Payload sent to the FastAPI server when completing a lesson.
 * Maps to the backend `LessonCompleteRequest` schema.
 */
data class LessonCompleteRequest(
    @SerializedName("run_id") val runId: String
)

/**
 * Response received from the FastAPI server after successfully finalizing a lesson.
 * Maps to the backend `LessonCompleteResponse` schema.
 */
data class LessonCompleteResponse(
    @SerializedName("lesson_id") val lessonId: String,
    @SerializedName("status") val status: ProgressStatus,
    @SerializedName("average_score") val averageScore: Int,
    @SerializedName("feedback_text") val feedbackText: String
)