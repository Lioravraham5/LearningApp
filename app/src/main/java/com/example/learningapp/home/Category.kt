package com.example.learningapp.home

import com.google.gson.annotations.SerializedName

data class Category(
    val id: String,
    val title: String,
    val description: String,
    //@SerializedName("icon") val iconUrl: String?,
    @SerializedName("total_lessons") val totalLessons: Int,
    @SerializedName("completed_lessons") val completedLessons: Int,
    @SerializedName("progress_percentage") val progressPercentage: Float
) {
    // BEST PRACTICE: Hardcoded icon for the UI.
    // Since it's not in the constructor, Retrofit ignores it when parsing the JSON.
    val iconUrl: Int
        get() = android.R.drawable.ic_menu_camera
}
