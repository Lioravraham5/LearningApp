package com.example.learningapp.lessonDetails

import com.google.gson.annotations.SerializedName

/**
 * The consolidated model containing all detailed information required for the Lesson Details screen.
 */
data class LessonDetails(
    val id: String,
    val title: String,
    val description: String,
    //@SerializedName("icon") val iconUrl: String?,
    @SerializedName("sentences_count") val sentencesCount: Int,
    @SerializedName("completed_sentences") val completedSentences: Int
) {
    // BEST PRACTICE: Hardcoded icon for the UI.
    // Since it's not in the constructor, Retrofit ignores it when parsing the JSON.
    val iconUrl: Int
        get() = android.R.drawable.ic_menu_info_details

    /**
     * Determines if the lesson has been started but not yet finished.
     * The UI will use this boolean to decide whether to show the "Resume" button.
     */
    val isInProgress: Boolean
        get() = completedSentences > 0 && completedSentences < sentencesCount
}
