package com.example.learningapp.lessonDetails

/**
 * The consolidated model containing all detailed information required for the Lesson Details screen.
 * This is deliberately separate from the lightweight [Lesson] model used in list views.
 */
data class LessonDetails(
    val id: String,
    val title: String,
    val description: String,
    //@SerializedName("icon") val iconUrl: String?,
    val sentencesCount: Int
) {
    // BEST PRACTICE: Hardcoded icon for the UI.
    // Since it's not in the constructor, Retrofit ignores it when parsing the JSON.
    val iconUrl: Int
        get() = android.R.drawable.ic_menu_info_details
}
