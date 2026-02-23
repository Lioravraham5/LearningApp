package com.example.learningapp.auth

// A generic class to represent the state of an authentication operation
sealed class AuthResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    // Represents a successful operation with data
    class Success<T>(data: T) : AuthResult<T>(data)
    // Represents a failed operation with an error message
    class Error<T>(message: String, data: T? = null) : AuthResult<T>(data, message)
}