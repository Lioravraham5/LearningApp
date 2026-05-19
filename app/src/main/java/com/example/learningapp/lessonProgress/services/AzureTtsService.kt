package com.example.learningapp.lessonProgress.services

import android.util.Log
import com.example.learningapp.BuildConfig
import com.example.learningapp.avatar.AvatarType
import com.microsoft.cognitiveservices.speech.SpeechConfig
import com.microsoft.cognitiveservices.speech.SpeechSynthesisOutputFormat
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

/**
 * Concrete implementation of [TtsService] using Microsoft Azure Cognitive Services.
 */
class AzureTtsService : TtsService {

    private val TAG = "AzureTtsService"

    // Internal mutable state for the viseme ID
    private val _currentVisemeId = MutableStateFlow(0)
    // Public immutable state exposed to the ViewModel
    override val currentVisemeId: StateFlow<Int> = _currentVisemeId.asStateFlow()
    private var speechConfig: SpeechConfig? = null
    private var synthesizer: SpeechSynthesizer? = null
    private var currentVoiceName: String = "en-US-Andrew:DragonHDLatestNeural"

    /**
     * Helper function to ensure Azure resources are initialized and alive.
     */
    private fun initializeAzure() {
        if (speechConfig == null) {
            speechConfig = SpeechConfig.fromSubscription(
                BuildConfig.AZURE_SPEECH_KEY,
                BuildConfig.AZURE_SPEECH_REGION
            ).apply {
                // Set the specific voice and output format
                speechSynthesisVoiceName = currentVoiceName
                setSpeechSynthesisOutputFormat(SpeechSynthesisOutputFormat.Riff24Khz16BitMonoPcm)
            }
        }

        if (synthesizer == null) {
            synthesizer = SpeechSynthesizer(speechConfig).apply {
                // Listen to Azure's events and update our StateFlow.
                VisemeReceived.addEventListener { _, eventArgs ->
                    _currentVisemeId.value = eventArgs.visemeId.toInt()
                }
            }
        }
    }

    override suspend fun speakText(text: String) {
        // BEST PRACTICE: Move blocking I/O operations off the Main thread.
        // This prevents the UI from freezing while Azure initializes or downloads audio.
        withContext(Dispatchers.IO) {
            try {
                // Ensure the engine is alive before we try to use it!
                initializeAzure()

                Log.d(TAG, "Starting to speak: $text")

                // SpeakText() blocks the current thread until playback is finished.
                // Since we are in Dispatchers.IO, this is safe and desirable.
                val result = synthesizer?.SpeakText(text)

                Log.d(TAG, "Speech finished with reason: ${result?.reason}")
            } catch (e: Exception) {
                Log.e(TAG, "Error during speech synthesis", e)
            } finally {
                // Always reset the mouth to closed (0) when speech ends or fails
                _currentVisemeId.value = 0
            }
        }
    }

    override fun stopSpeaking() {
        try {
            Log.d(TAG, "Stopping speech")
            synthesizer?.StopSpeakingAsync()
            _currentVisemeId.value = 0
        } catch (e: Exception) {
            Log.e(TAG, "Failed to stop speech", e)
        }
    }

    override fun setAvatarVoice(avatarType: AvatarType) {
        val newVoiceName = when (avatarType) {
            AvatarType.MALE -> "en-US-Andrew:DragonHDLatestNeural"
            AvatarType.FEMALE -> "en-us-Aria:DragonHDLatestNeural"
        }

        // Only re-initialize if the voice actually changed
        if (currentVoiceName != newVoiceName) {
            currentVoiceName = newVoiceName
            Log.d(TAG, "Voice changed to $currentVoiceName. Forcing re-initialization.")

            // By shutting down, we safely clear the old instances.
            shutdown()
        }
    }

    override fun shutdown() {
        try {
            Log.d(TAG, "Shutting down synthesizer resources")
            synthesizer?.close()
            speechConfig?.close()
            synthesizer = null
            speechConfig = null
        } catch (e: Exception) {
            Log.e(TAG, "Error closing Azure resources", e)
        }
    }
}