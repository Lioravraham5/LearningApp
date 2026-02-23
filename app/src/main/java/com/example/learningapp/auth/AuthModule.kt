package com.example.learningapp.auth

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // This makes the dependencies live as long as the app is running
object AuthModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        // Return the standard Firebase Auth instance
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository {
        // We tell Hilt: whenever someone asks for AuthRepository,
        // give them an instance of FirebaseAuthRepositoryImpl
        return FirebaseAuthRepositoryImpl(firebaseAuth)
    }
}