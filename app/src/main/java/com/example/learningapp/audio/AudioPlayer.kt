package com.example.learningapp.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import com.example.learningapp.R

object AudioPlayer {
    fun playSample(context: Context, onDone: (() -> Unit)? = null) {
        val mp = MediaPlayer.create(context, R.raw.sample)
        mp.setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
        )
        mp.setOnCompletionListener {
            it.release()
            onDone?.invoke()
        }
        mp.start()
    }
}