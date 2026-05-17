package com.example.learningapp.lessonProgress.models

import com.google.gson.annotations.SerializedName

/**
 * Represents a single sentence within a lesson.
 */
data class Sentence(
    val id: String,
    val text: String,
    @SerializedName("order_index") val orderIndex: Int
)