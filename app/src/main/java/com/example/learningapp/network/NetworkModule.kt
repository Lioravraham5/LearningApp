package com.example.learningapp.network

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Hilt module that provides network-related dependencies.
 * * BEST PRACTICE: By providing these as Singletons, we reuse the same OkHttpClient
 * and Retrofit instances across the entire app, saving memory and connection overhead.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // IMPORTANT:
    // If you are testing on an Android Emulator, use "http://10.0.2.2:8000/"
    // If you are testing on a physical device, use your computer's local Wi-Fi IP (e.g., "http://192.168.1.15:8000/")
    //private const val BASE_URL = "http://10.0.2.2:8000/"
    private const val BASE_URL = "http://10.0.2.2:8000/"

    @Provides
    @Singleton
    fun provideAuthInterceptor(firebaseAuth: FirebaseAuth): AuthInterceptor {
        return AuthInterceptor(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        // HttpLoggingInterceptor is crucial for debugging.
        // It prints the exact JSON requests and responses to the Logcat.
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor) // 1. Attaches the Firebase Token
            .addInterceptor(loggingInterceptor) // 2. Logs the network traffic
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .callTimeout(180, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()) // Converts JSON to Kotlin Data Classes
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}