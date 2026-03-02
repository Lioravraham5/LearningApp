package com.example.learningapp.auth

import com.google.firebase.auth.FirebaseUser


interface AuthRepository {
    // Executes login and suspends until the result is available from Firebase
    suspend fun login(email: String, password: String): AuthResult<FirebaseUser>

    // Executes login with Google and suspends until the result is available
    suspend fun loginWithGoogle(idToken: String): AuthResult<FirebaseUser>

    // Executes registration and suspends until the result is available
    suspend fun register(email: String, password: String): AuthResult<FirebaseUser>

    // Get the currently logged-in user, if any
    fun getCurrentUser(): FirebaseUser?

    // Sign out the current user
    fun logout()

    // Updates the user's display name in Firebase
    suspend fun updateDisplayName(newName: String): AuthResult<Unit>

    // Permanently deletes the user's account and data from Firebase
    suspend fun deleteAccount(): AuthResult<Unit>
}