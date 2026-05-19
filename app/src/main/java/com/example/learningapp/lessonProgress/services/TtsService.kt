package com.example.learningapp.lessonProgress.services

import com.example.learningapp.avatar.AvatarType
import kotlinx.coroutines.flow.StateFlow

/**
 * Interface defining the contract for Text-To-Speech capabilities.
 */
interface TtsService {
    /**
     * A reactive stream representing the current mouth shape (viseme).
     */
    val currentVisemeId: StateFlow<Int>

    /**
     * Speaks the given text aloud.
     * @param text The text to be spoken.
     */
    suspend fun speakText(text: String)

    /**
     * Immediately stops any ongoing speech.
     */
    fun stopSpeaking()

    /**
     * Sets the avatar's voice according to the provided type.
     */
    fun setAvatarVoice(avatarType: AvatarType)

    /**
     * Cleans up resources. Must be called when the Service is no longer needed
     */
    fun shutdown()
}