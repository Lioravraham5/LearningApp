package com.example.learningapp.auth.login

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
 * State class for the Login screen.
 */
data class LoginState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    // Hilt will provide the repository using the AuthModule
    private val repository: AuthRepository
) : ViewModel() {

    val TAG = "LoginViewModel"

    // Internal state (Mutable)
    private val _loginState = MutableStateFlow(LoginState())
    // Public state (Immutable) for the UI to observe
    val loginState = _loginState.asStateFlow()

    /**
     * Executes the login process using the provided credentials.
     * Communicates the result back to the UI via the "loginState".
     */
    fun login(email: String, password: String) {
        // Basic validation before calling the repository
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginState(error = "Please fill in all fields")
            return
        }

        // Starts a coroutine on the Main Thread.
        // Note: Even though it starts on Main, calling suspended repository methods will release the main thread and won't clock it
        viewModelScope.launch {
            // Update state to loading
            _loginState.value = LoginState(isLoading = true)

            // Call the suspended login method from the repository
            val result = repository.login(email, password)

            when (result) {
                is AuthResult.Success -> {
                    Log.d(TAG, "Login successful for user: $email")
                    // Update state to success
                    _loginState.value = LoginState(isSuccess = true)
                }
                is AuthResult.Error -> {
                    Log.d(TAG, "Login failed for user: $email. Error: ${result.message}")
                    // Update state with the error message returned by Firebase
                    _loginState.value = LoginState(error = result.message)
                }
            }
        }
    }

    /**
     * Resets the error message so it doesn't reappear after being dismissed.
     */
    fun clearError() {
        _loginState.value = _loginState.value.copy(error = null)
    }
}