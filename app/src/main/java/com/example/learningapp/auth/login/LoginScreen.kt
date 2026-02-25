package com.example.learningapp.auth.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.learningapp.auth.AuthPasswordTextField
import com.example.learningapp.auth.AuthTextField

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit

) {
    // Collecting the state from ViewModel
    val state by viewModel.loginState.collectAsStateWithLifecycle()

    LoginContent(
        state = state,
        onLoginClick = { email, password -> viewModel.login(email, password) },
        onNavigateToRegister = onNavigateToRegister,
        onLoginSuccess = onLoginSuccess
    )
}


/**
 * A stateless version of the Login screen, used for better testability and Previews.
 */
@Composable
fun LoginContent(
    state: LoginState,
    onLoginClick: (String, String) -> Unit,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) { 
    // Local state for the input fields
    var email by remember { mutableStateOf("Lior@gmail.com") }
    var password by remember { mutableStateOf("123456") }

    // React to success state
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = "Welcome Back", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(32.dp))

        AuthTextField(value = email, onValueChange = { email = it }, label = "Email")

        Spacer(modifier = Modifier.height(16.dp))

        AuthPasswordTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password"
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { onLoginClick(email, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
        }

        // Error message display
        state.error?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation to Register
        Row {
            Text("Don't have an account? ")
            Text(
                text = "Sign Up",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onNavigateToRegister() }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    // Refactored to use LoginContent with a static state to avoid ViewModel instantiation issues in Preview.
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            LoginContent(
                state = LoginState(),
                onLoginClick = { _, _ -> },
                onNavigateToRegister = {},
                onLoginSuccess = {}
            )
        }
    }
}