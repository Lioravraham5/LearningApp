package com.example.learningapp.audio

import android.content.Context
import com.example.learningapp.R
import java.io.File
import java.io.FileOutputStream

object SampleAudio {
    fun ensureSampleWavFile(context: Context): File {
        val outFile = File(context.filesDir, "sample.wav")
        if (outFile.exists()) return outFile

        context.resources.openRawResource(R.raw.sample).use { input ->
            FileOutputStream(outFile).use { output ->
                input.copyTo(output)
            }
        }
        return outFile
    }
}