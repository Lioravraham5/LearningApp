package com.example.learningapp.categoryDetails

import com.google.gson.annotations.SerializedName

/**
 * The consolidated model containing all data required for the Category Details screen.
 * We use this specific model for now to avoid altering the existing Category models.
 */
data class CategoryDetails(
    val id: String,
    val title: String,
    val description: String,
    @SerializedName("icon") val iconUrl: String?,
    val lessons: List<Lesson>
)

