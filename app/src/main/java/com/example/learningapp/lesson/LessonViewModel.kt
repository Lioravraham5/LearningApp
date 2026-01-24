package com.example.learningapp.lesson

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.microsoft.cognitiveservices.speech.SpeechConfig
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.learningapp.BuildConfig

class LessonViewModel : ViewModel() {

    var currentVisemeId by mutableIntStateOf(0)

    private val speechKey = BuildConfig.AZURE_SPEECH_KEY
    private val serviceRegion = BuildConfig.AZURE_SPEECH_REGION


    fun speak(text: String) {
        // Launch a coroutine on the IO dispatcher to handle network and background processing without freezing the UI.
        viewModelScope.launch(Dispatchers.IO) {
            // Initialize the Speech Configuration using your subscription key and service region
            val config = SpeechConfig.fromSubscription(speechKey, serviceRegion)
            // Specify the voice to be used
            config.speechSynthesisVoiceName = "en-US-Andrew:DragonHDLatestNeural"

            // Create the SpeechSynthesizer object with the specified configuration.
            val synthesizer = SpeechSynthesizer(config)

            // Add a listener for Viseme events.
            // This is triggered every time a new mouth shape is calculated for the current phoneme.
            synthesizer.VisemeReceived.addEventListener { _, e ->
                // Update the currentVisemeId state. This change will be observed by the Compose UI to update the mouth image.
                currentVisemeId = e.visemeId.toInt()
            }

            try {
                // Start the speech synthesis process. The audio plays through the default speaker while visemes are sent simultaneously.
                synthesizer.SpeakText(text)
            } catch (e: Exception) {
                // Handle any potential errors such as network issues or invalid keys.
                e.printStackTrace()
            } finally {
                // Reset the avatar's mouth to a closed position (ID 0) once the speaking is finished.
                currentVisemeId = 0

                // Dispose of the synthesizer to free up system resources and prevent memory leaks.
                synthesizer.close()
            }
        }
    }

}