package com.example.learningapp.network

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import kotlin.concurrent.thread

object AsrApi {
    private val httpClient = OkHttpClient()

    fun sendAudioToAsr(
        audioFile: File,
        targetSentence: String,
        onResult: (String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        thread {
            try {
                val fileBody = audioFile.asRequestBody("audio/*".toMediaType())
                val multipart = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", audioFile.name, fileBody)
                    .addFormDataPart("target_sentence", targetSentence)
                    .build()

                val request = Request.Builder()
                    .url("http://10.0.2.2:8000/asr")
                    .post(multipart)
                    .build()

                httpClient.newCall(request).execute().use { resp ->
                    val body = resp.body?.string().orEmpty()
                    if (!resp.isSuccessful) throw RuntimeException("ASR failed: ${resp.code} $body")
                    onResult(body)
                }
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }
}