package com.example.learningapp.lessonProgress

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningapp.core.UserSettingsRepository
import com.example.learningapp.lessonProgress.services.AudioRecorderService
import com.example.learningapp.lessonProgress.services.TtsService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Lesson Progress Screen.
 * It manages the State Machine of the lesson and coordinates between hardware (Mic/TTS) and the network.
 */
@HiltViewModel
class LessonProgressViewModel @Inject constructor(
    private val repository: LessonProgressRepository,
    private val ttsService: TtsService,
    private val audioRecorderService: AudioRecorderService,
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    private val TAG = "LessonProgressViewModel"

    private val _uiState = MutableStateFlow(LessonProgressState())
    val uiState: StateFlow<LessonProgressState> = _uiState.asStateFlow()

    init {
        // Listen to Avatar's mouth movements (visemes) and push them to the UI state.
        viewModelScope.launch {
            ttsService.currentVisemeId.collect { visemeId ->
                _uiState.update { it.copy(currentVisemeId = visemeId) }
            }
        }

        // Listen to user avatar type changes and push them to the UI state.
        viewModelScope.launch {
            userSettingsRepository.avatarTypeFlow.collect { savedAvatarType ->
                _uiState.update { it.copy(avatarType = savedAvatarType) }
                ttsService.setAvatarVoice(savedAvatarType)
            }
        }
    }

    /**
     * Step A: Initialization. Fetches the sentences from the server.
     */
    fun loadLesson(lessonId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(step = LessonStep.LOADING_SENTENCES, errorMessage = null) }

            val result = repository.getLessonSentences(lessonId)

            result.onSuccess { sentencesList ->
                _uiState.update {
                    it.copy(
                        sentences = sentencesList,
                        currentSentenceIndex = 0,
                        step = LessonStep.READY_TO_START
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(errorMessage = "Failed to load lesson: ${error.localizedMessage}")
                }
            }
        }
    }

    /**
     * Step B: Avatar Speaks. Plays the current target sentence.
     * Can be used for the initial Play and for Replay.
     */
    fun playCurrentSentence() {
        val currentSentenceText = _uiState.value.currentSentence?.text ?: return

        viewModelScope.launch {
            // Stop any ongoing speech just in case the user clicked quickly
            ttsService.stopSpeaking()

            // Update UI to show the Avatar is speaking
            _uiState.update { it.copy(step = LessonStep.AVATAR_SPEAKING) }

            // This is a suspend function, so it will pause here until Azure finishes speaking
            ttsService.speakText(currentSentenceText)

            // Once speech is finished, move to the waiting state so the user can press the Mic
            _uiState.update { it.copy(step = LessonStep.WAITING_FOR_RECORDING) }
        }
    }

    /**
     * Step C (Start): User presses the Microphone button.
     */
    fun startRecording() {
        try {
            audioRecorderService.startRecording()
            _uiState.update { it.copy(step = LessonStep.RECORDING, errorMessage = null) }
        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = "Failed to open microphone.") }
        }
    }

    /**
     * Step C (Stop) & Step D: User stops recording, upload to server, and get analysis.
     */
    fun stopRecordingAndAnalyze() {
        val audioFile = audioRecorderService.stopRecording()
        // SHIFT: We now extract the ID instead of the text
        val currentSentenceId = _uiState.value.currentSentence?.id

        if (audioFile == null || currentSentenceId == null) {
            _uiState.update {
                it.copy(errorMessage = "Recording failed.", step = LessonStep.WAITING_FOR_RECORDING)
            }
            return
        }

        viewModelScope.launch {
            // Change state to show a loading spinner / analyzing animation
            _uiState.update { it.copy(step = LessonStep.ANALYZING) }

            // SHIFT: Passing the ID to the repository
            val result = repository.evaluateSpeech(audioFile, currentSentenceId)

            result.onSuccess { evaluation ->
                // Move to Feedback step and store the evaluation data
                _uiState.update {
                    it.copy(
                        step = LessonStep.SHOWING_FEEDBACK,
                        currentEvaluation = evaluation
                    )
                }

                // Step E: Avatar reads the feedback dynamically!
                // SHIFT: We simply pass the feedbackText string generated directly by the server
                val feedbackSpokenText = evaluation.feedbackText
                ttsService.speakText(feedbackSpokenText)

            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        errorMessage = error.localizedMessage ?: "Analysis failed",
                        step = LessonStep.WAITING_FOR_RECORDING // Let them try recording again
                    )
                }
            }
        }
    }

    /**
     * Step E (Next): User clicks the "Next" arrow after reading/hearing the feedback.
     */
    fun moveToNextSentence() {
        val currentState = _uiState.value
        val nextIndex = currentState.currentSentenceIndex + 1

        // Stop TTS in case the user clicked "Next" while the avatar was still reading feedback
        ttsService.stopSpeaking()

        if (nextIndex < currentState.sentences.size) {
            // Move to next sentence, reset state
            _uiState.update {
                it.copy(
                    currentSentenceIndex = nextIndex,
                    step = LessonStep.READY_TO_START,
                    currentEvaluation = null, // Clear previous score/feedback
                    errorMessage = null
                )
            }
        } else {
            // Lesson is completely finished
            _uiState.update { it.copy(step = LessonStep.LESSON_COMPLETED) }
        }
    }

    /**
     * Get the current amplitude for the visualizer UI (Voice Waves).
     */
    fun getRecordingAmplitude(): Int {
        return audioRecorderService.getMaxAmplitude()
    }

    /**
     * Release hardware resources when the ViewModel dies.
     */
    override fun onCleared() {
        ttsService.shutdown()
        audioRecorderService.stopRecording() // Just in case it was left open
        super.onCleared()
    }

    /**
     * Handle app going to the background: Stops any active hardware usage (TTS or Mic) and resets the state safely.
     */
    fun onAppBackgrounded() {
        val currentState = _uiState.value
        Log.w(TAG, "[LIFECYCLE] onAppBackgrounded triggered! Current UI Step: ${currentState.step}")
        // 1. If the avatar is speaking, stop the TTS engine completely
        if (currentState.step == LessonStep.AVATAR_SPEAKING || currentState.step == LessonStep.SHOWING_FEEDBACK) {
            Log.d(TAG, "[LIFECYCLE] Avatar was speaking. Calling ttsService.shutdown()")
            ttsService.shutdown()
            if (currentState.step == LessonStep.AVATAR_SPEAKING) {
                Log.d(TAG, "[LIFECYCLE] Resetting state to READY_TO_START")
                _uiState.update { it.copy(step = LessonStep.READY_TO_START) }
            }
        }

        // 2. If the user was recording, stop the microphone to release the resource
        if (currentState.step == LessonStep.RECORDING) {
            Log.d(TAG, "[LIFECYCLE] User was recording. Stopping mic.")
            audioRecorderService.stopRecording()
            _uiState.update {
                it.copy(
                    step = LessonStep.WAITING_FOR_RECORDING,
                    errorMessage = "Recording paused."
                )
            }
        }
    }
}