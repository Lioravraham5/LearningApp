package com.example.learningapp.lessonProgress.services

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import java.io.File
import java.io.IOException

/**
 * Concrete implementation of [AudioRecorderService] using Android's native MediaRecorder.
 */
class AndroidAudioRecorderService(
    private val context: Context
) : AudioRecorderService {

    private val TAG = "AndroidAudioRecorderService"

    private var recorder: MediaRecorder? = null
    private var outputFile: File? = null
    private var isRecording = false

    override fun startRecording() {
        if (isRecording) {
            Log.w(TAG, "Recording is already in progress.")
            return
        }

        try {
            // BEST PRACTICE: Save the recording in the app's internal cache directory.
            // This ensures the file is private to our app and gets deleted automatically by the OS
            // if the device runs low on storage. No need to clutter the user's public audio folders.
            outputFile = File(context.cacheDir, "lesson_user_recording.m4a")

            // Initialize MediaRecorder based on the Android API version
            recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context) // // For Android 12+ (API 31+)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder() // Fallback for Android 8-11 (API 26-30)
            }

            recorder?.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)

                // Using MPEG_4 and AAC encoder creates a highly compressed, high-quality audio file.
                // This is crucial for fast network uploads to the FastAPI server. Whisper supports it natively.
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

                // Set the output file path
                setOutputFile(outputFile?.absolutePath)

                // Prepare and start recording
                prepare()
                start()
            }

            isRecording = true
            Log.d(TAG, "Successfully started recording to: ${outputFile?.absolutePath}")

        } catch (e: IOException) {
            Log.e(TAG, "Failed to prepare MediaRecorder", e)
            resetRecorder()
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error starting MediaRecorder", e)
            resetRecorder()
        }
    }

    override fun stopRecording(): File? {
        if (!isRecording) {
            Log.w(TAG, "Cannot stop recording because it is not currently running.")
            return null
        }

        return try {
            recorder?.apply {
                stop()
                release()
            }
            Log.d(TAG, "Successfully stopped recording.")

            // Return the recorded file so the Repository can upload it
            outputFile
        } catch (e: Exception) {
            // Sometimes MediaRecorder throws an exception if stop() is called immediately after start()
            Log.e(TAG, "Failed to stop MediaRecorder properly", e)
            null
        } finally {
            resetRecorder()
        }
    }

    override fun getMaxAmplitude(): Int {
        return if (isRecording) {
            // Returns the highest amplitude value since the last time this was called.
            // Will be used by a Coroutine loop in the ViewModel to animate the visualizer.
            recorder?.maxAmplitude ?: 0
        } else {
            0
        }
    }

    /**
     * Cleans up the recorder state.
     */
    private fun resetRecorder() {
        recorder?.release()
        recorder = null
        isRecording = false
    }
}