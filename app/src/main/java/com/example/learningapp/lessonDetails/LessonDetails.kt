package com.example.learningapp.lessonDetails

import com.google.gson.annotations.SerializedName

/**
 * The consolidated model containing all detailed information required for the Lesson Details screen.
 * Note: Under the new run_id architecture, [completedSentences] specifically represents
 * the progress of the user's CURRENT active run, not their all-time historical progress.
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
     * Determines if this is a completely fresh run with 0 sentences completed.
     * The UI uses this to show a primary "Start Lesson" button.
     */
    val isNotStarted: Boolean
        get() = completedSentences == 0

    /**
     * Determines if the lesson has been started but not yet finished in the CURRENT run.
     * The UI will use this boolean to decide whether to show the "Resume" and "Restart" buttons.
     */
    val isInProgress: Boolean
        get() = completedSentences > 0 && completedSentences < sentencesCount

    /**
     * Determines if the user has completed all sentences in the CURRENT active run.
     * The UI uses this to show a "Practice Again" button instead of "Resume".
     */
    val isCompleted: Boolean
        get() = sentencesCount > 0 && completedSentences >= sentencesCount
}