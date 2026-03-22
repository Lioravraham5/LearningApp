package com.example.learningapp.progress

import com.google.gson.annotations.SerializedName

/**
 * Represents a badge in the "Badges" tab.
 */
data class Badge(
    val id: String,
    val title: String,
    val description: String,
    //@SerializedName("icon") val iconUrl: String?,
    @SerializedName("is_achieved") val isAchieved: Boolean
) {
    val iconUrl: Int
        get() = if (isAchieved) android.R.drawable.star_on else android.R.drawable.star_off

}
