package com.example.learningapp.lessonProgress.services

import android.util.Log
import com.example.learningapp.BuildConfig
import com.example.learningapp.avatar.AvatarType
import com.microsoft.cognitiveservices.speech.SpeechConfig
import com.microsoft.cognitiveservices.speech.SpeechSynthesisOutputFormat
import com.microsoft.cognitiveservices.speech.SpeechSynthesisVisemeEventArgs
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer
import com.microsoft.cognitiveservices.speech.util.EventHandler
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

    private val visemeListener = EventHandler<SpeechSynthesisVisemeEventArgs> { _, eventArgs ->
        Log.d(TAG, "[VISEME EVENT] Received viseme ID: ${eventArgs.visemeId.toInt()}. from Azure")
        _currentVisemeId.value = eventArgs.visemeId.toInt()
    }

    /**
     * Helper function to ensure Azure resources are initialized and alive.
     */
    private fun initializeAzure() {
        if (speechConfig == null) {
            Log.d(TAG, "[initializeAzure] Creating new SpeechConfig with voice: $currentVoiceName")
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
            Log.d(TAG, "[initializeAzure] Building new SpeechSynthesizer instance")
            synthesizer = SpeechSynthesizer(speechConfig)
        } else {
            Log.d(TAG, "[initializeAzure] Synthesizer already exists. Reusing it.")
        }
    }

    override suspend fun speakText(text: String) {
        // BEST PRACTICE: Move blocking I/O operations off the Main thread.
        // This prevents the UI from freezing while Azure initializes or downloads audio.
        withContext(Dispatchers.IO) {
            try {
                // Ensure the engine is alive before we try to use it!
                initializeAzure()

                Log.d(TAG, "[speakText] Starting to speak: '$text'")
                // 1. CONNECT the pipe exactly when we need it
                synthesizer?.VisemeReceived?.addEventListener(visemeListener)
                // SpeakText() blocks the current thread until playback is finished.
                // Since we are in Dispatchers.IO, this is safe and desirable.
                val result = synthesizer?.SpeakText(text)

                Log.d(TAG, "[speakText] Speech finished. Reason: ${result?.reason}")
            } catch (e: Exception) {
                Log.e(TAG, "[speakText] Error during speech synthesis", e)
            } finally {
                // 2. DISCONNECT the pipe securely when finished (or if an error occurred)
                synthesizer?.VisemeReceived?.removeEventListener(visemeListener)
                // Always reset the mouth to closed (0) when speech ends or fails
                Log.d(TAG, "[speakText] Finally block reached. Resetting mouth to 0.")
                _currentVisemeId.value = 0
            }
        }
    }

    override fun stopSpeaking() {
        try {
            Log.w(TAG, "[stopSpeaking] stopSpeaking() called! Setting mouth to 0 and asking Azure to stop.")
            // DISCONNECT the pipe instantly!
            // Any trailing ghost visemes from Azure will hit a dead end because we are no longer listening.
            synthesizer?.VisemeReceived?.removeEventListener(visemeListener)
            synthesizer?.StopSpeakingAsync()?.get()
            _currentVisemeId.value = 0
        } catch (e: Exception) {
            Log.e(TAG, "[stopSpeaking] Failed to stop speech", e)
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
            Log.d(TAG, "[setAvatarVoice] Voice changed to $currentVoiceName. Calling shutdown().")

            // By shutting down, we safely clear the old instances.
            shutdown()
        }
    }

    override fun shutdown() {
        try {
            Log.w(TAG, "[shutdown] Shutting down synthesizer resources completely.")
            // Ensure listener is cleanly removed before destruction
            synthesizer?.VisemeReceived?.removeEventListener(visemeListener)
            // Explicitly stop active audio playback before closing the object.
            synthesizer?.StopSpeakingAsync()?.get()
            synthesizer?.close()
            speechConfig?.close()
            synthesizer = null
            speechConfig = null
        } catch (e: Exception) {
            Log.e(TAG, "[shutdown] Error closing Azure resources", e)
        }
    }
}