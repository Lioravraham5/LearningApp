package com.example.learningapp.lessonProgress.services

import java.io.File

/**
 * Interface defining the contract for audio recording capabilities.
 */
interface AudioRecorderService {

    /**
     * Starts recording audio from the device's microphone.
     * Note: The UI layer MUST ensure that the RECORD_AUDIO permission is granted before calling this.
     *
     * @throws IllegalStateException if the recorder is already running or fails to start.
     */
    fun startRecording()

    /**
     * Stops the ongoing recording and returns the generated audio file.
     *
     * @return The recorded audio [File], or null if recording failed or wasn't started.
     */
    fun stopRecording(): File?

    /**
     * Returns the maximum absolute amplitude sampled since the last call to this method.
     * This is used to drive the Audio Visualizer UI (voice waves) in real-time.
     *
     * @return An integer representing the amplitude (usually between 0 and 32767).
     */
    fun getMaxAmplitude(): Int
}