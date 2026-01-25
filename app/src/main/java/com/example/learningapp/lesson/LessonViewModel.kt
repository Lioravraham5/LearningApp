package com.example.learningapp.lesson

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningapp.BuildConfig
import com.microsoft.cognitiveservices.speech.SpeechConfig
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer
import com.microsoft.cognitiveservices.speech.SpeechSynthesisOutputFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LessonViewModel : ViewModel() {

    var currentVisemeId by mutableIntStateOf(0)
    private val speechKey = BuildConfig.AZURE_SPEECH_KEY
    private val serviceRegion = BuildConfig.AZURE_SPEECH_REGION
    private val config: SpeechConfig by lazy {
        SpeechConfig.fromSubscription(speechKey, serviceRegion).apply {
            speechSynthesisVoiceName = "en-US-Andrew:DragonHDLatestNeural"
            setSpeechSynthesisOutputFormat(SpeechSynthesisOutputFormat.Riff24Khz16BitMonoPcm)
        }
    }

    private val synthesizer: SpeechSynthesizer by lazy {
        SpeechSynthesizer(config).also { syn ->
            syn.VisemeReceived.addEventListener { _, e ->
                currentVisemeId = e.visemeId.toInt()
            }
        }
    }

    fun speak(text: String) = speak(text, onDone = null)

    fun speak(text: String, onDone: (() -> Unit)?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Stop any previous speech to avoid overlap/glitches
                synthesizer.SpeakText(text) // blocks until playback finished
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                currentVisemeId = 0
                if (onDone != null) {
                    withContext(Dispatchers.Main) { onDone() }
                }
            }
        }
    }

    override fun onCleared() {
        try { synthesizer.close() } catch (_: Exception) {}
        super.onCleared()
    }
}