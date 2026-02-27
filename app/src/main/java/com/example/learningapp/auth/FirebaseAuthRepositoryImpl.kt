package com.example.learningapp.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
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

    override suspend fun loginWithGoogle(idToken: String): AuthResult<FirebaseUser> {
        return try {
            // Create a Firebase credential using the Google ID token we received
            val credential = GoogleAuthProvider.getCredential(idToken, null)

            // Authenticate with Firebase using this credential
            val result = firebaseAuth.signInWithCredential(credential).await()

            val user = result.user
            if (user != null) {
                AuthResult.Success(user)
            } else {
                AuthResult.Error("Google sign-in succeeded, but user data is null")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.localizedMessage ?: "Google sign-in failed")
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