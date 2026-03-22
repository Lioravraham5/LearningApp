package com.example.learningapp.network

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * An OkHttp Interceptor that automatically attaches the Firebase ID Token
 * to every outbound HTTP request.
 *
 * BEST PRACTICE: This ensures that our Retrofit API calls are always authenticated
 * without having to manually pass the token to every single repository method.
 */
class AuthInterceptor @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : Interceptor {

    private val TAG = "AuthInterceptor"

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // 1. Get the current authenticated Firebase user
        val currentUser = firebaseAuth.currentUser

        // If there is no user logged in, proceed with the original unauthenticated request
        if (currentUser == null) {
            Log.d(TAG, "No user logged in. Proceeding without Auth token.")
            return chain.proceed(originalRequest)
        }

        // 2. Fetch the ID token synchronously
        // EXPLANATION: OkHttp Interceptors run on background threads.
        // Firebase's getIdToken() returns an async Task. To bridge the async Task
        // with OkHttp's synchronous chain, we safely use runBlocking here.
        val token: String? = try {
            runBlocking {
                // Passing 'false' means Firebase will return the cached token if it's still valid,
                // or automatically fetch a fresh one from the server if it has expired.
                val result = currentUser.getIdToken(false).await()
                result.token
            }
        } catch (e: Exception) {
            Log.d(TAG, "Failed to fetch Firebase ID token", e)
            null
        }

        // 3. Attach the token to the header
        return if (token != null) {
            // Clone the original request and add the Authorization header
            val authorizedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()

            Log.d(TAG, "Successfully attached Bearer token to request: ${originalRequest.url}")
            chain.proceed(authorizedRequest)
        } else {
            // If token fetching failed (e.g., network error), proceed without it.
            // The backend will catch the missing token and return a 401 Unauthorized.
            chain.proceed(originalRequest)
        }
    }
}