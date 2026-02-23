package com.example.learningapp.auth

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    // Executes login and suspends until the result is available from Firebase
    suspend fun login(email: String, password: String): AuthResult<FirebaseUser>

    // Executes registration and suspends until the result is available
    suspend fun register(email: String, password: String): AuthResult<FirebaseUser>

    // Get the currently logged-in user, if any
    fun getCurrentUser(): FirebaseUser?

    // Sign out the current user
    fun logout()
}