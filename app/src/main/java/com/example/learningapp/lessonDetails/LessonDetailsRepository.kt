package com.example.learningapp.lessonDetails

/**
 * An abstraction for the Lesson data layer.
 * By using an interface, the ViewModel doesn't need to know if the data comes
 * from a mock, a local database, or a remote server. This enforces Clean Architecture.
 */
interface LessonDetailsRepository {
    /**
     * Fetches the complete details for a specific lesson.
     * @param lessonId The unique identifier of the lesson.
     * @return [LessonDetails] containing the descriptive info and sentence count.
     * @throws Exception if the lesson is not found or a network error occurs.
     */
    suspend fun getLessonDetails(lessonId: String): LessonDetails
}