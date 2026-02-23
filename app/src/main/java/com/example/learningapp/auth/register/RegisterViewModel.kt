package com.example.learningapp.auth.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningapp.auth.AuthRepository
import com.example.learningapp.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * State class for the Registration screen.
 */
data class RegisterState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    val TAG = "RegisterViewModel"

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()

    /**
     * Executes the register process using the provided credentials.
     * Communicates the result back to the UI via the "registerState".
     */
    fun register(email: String, password: String, confirmPassword: String) {
        // Basic empty field validation
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _registerState.value = RegisterState(error = "All fields are required")
            return
        }

        // Password match validation
        if (password != confirmPassword) {
            _registerState.value = RegisterState(error = "Passwords do not match")
            return
        }

        // Password length validation (Optional, but recommended)
        if (password.length < 6) {
            _registerState.value = RegisterState(error = "Password must be at least 6 characters")
            return
        }

        // Starts a coroutine on the Main Thread.
        // Note: Even though it starts on Main, calling suspended repository methods will release the main thread and won't clock it
        viewModelScope.launch {
            _registerState.value = RegisterState(isLoading = true)

            val result = repository.register(email, password)

            when (result) {
                is AuthResult.Success -> {
                    Log.d(TAG, "Register successful for user: $email")
                    _registerState.value = RegisterState(isSuccess = true)
                }
                is AuthResult.Error -> {
                    Log.d(TAG, "Register failed for user: $email. Error: ${result.message}")
                    _registerState.value = RegisterState(error = result.message)
                }
            }
        }
    }

    /**
     * Resets the error message so it doesn't reappear after being dismissed.
     */
    fun clearError() {
        _registerState.value = _registerState.value.copy(error = null)
    }
}