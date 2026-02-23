package com.example.learningapp.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    override suspend fun login(
        email: String,
        password: String
    ): AuthResult<FirebaseUser> {
        return try {
            // Suspends until signIn completes or fails
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()

            val user = result.user
            if (user != null) {
                AuthResult.Success(user)
            } else {
                AuthResult.Error("User data is empty")
            }
        } catch (e: Exception) {
            // Maps Firebase exceptions to our AuthResult.Error
            AuthResult.Error(e.localizedMessage ?: "An unexpected error occurred during login")
        }
    }

    override suspend fun register(
        email: String,
        password: String
    ): AuthResult<FirebaseUser> {
        return try {
            // Suspends until createUser completes or fails
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

            val user = result.user
            if (user != null) {
                AuthResult.Success(user)
            } else {
                AuthResult.Error("User creation failed")
            }
        } catch (e: Exception) {
            // Catches errors like "Email already in use" or "Weak password"
            AuthResult.Error(e.localizedMessage ?: "Registration failed")
        }
    }


    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}