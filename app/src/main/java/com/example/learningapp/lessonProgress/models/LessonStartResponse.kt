package com.example.learningapp.lessonProgress.models

import com.google.gson.annotations.SerializedName

/**
 * Response model received when initializing a new run for a lesson.
 * The client MUST hold onto this [runId] and send it with every ASR request.
 */
data class LessonStartResponse(
    @SerializedName("run_id") val runId: String
)