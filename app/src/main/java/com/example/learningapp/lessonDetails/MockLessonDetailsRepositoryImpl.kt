package com.example.learningapp.lessonDetails

import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * A mock implementation of the repository to simulate network responses.
 * This is perfect for building and testing the UI independently before the backend is ready.
 */
class MockLessonDetailsRepositoryImpl @Inject constructor() : LessonDetailsRepository {

    override suspend fun getLessonDetails(lessonId: String): LessonDetails {
        // BEST PRACTICE: Simulate network latency so we can actually see and test
        // the Loading state (CircularProgressIndicator) in our Compose UI.
        delay(1000)

        // Simulating a database/server lookup based on the requested ID.
        // Note: These IDs match the mock data we previously created in CategoryDetails.
        return when (lessonId) {
            "l1_1" -> LessonDetails(
                id = "l1_1",
                title = "Introduction to Interviews",
                description = "Learn the basic vocabulary and etiquette for starting a professional job interview in English.",
                sentencesCount = 15,
                completedSentences = 15
            )
            "l1_2" -> LessonDetails(
                id = "l1_2",
                title = "Answering 'Tell me about yourself'",
                description = "Master the perfect elevator pitch to introduce yourself confidently to recruiters.",
                sentencesCount = 20,
                completedSentences = 8
            )
            "l2_1" -> LessonDetails(
                id = "l2_1",
                title = "At the Airport",
                description = "Essential phrases for check-in, security, and finding your boarding gate.",
                sentencesCount = 12,
                completedSentences = 0
            )
            // Simulating a 404 Not Found error from a server for any other ID
            else -> throw IllegalArgumentException("Lesson with ID $lessonId not found on the mock server.")
        }
    }
}