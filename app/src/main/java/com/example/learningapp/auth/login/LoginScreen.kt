package com.example.learningapp.auth.login

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.learningapp.auth.AuthPasswordTextField
import com.example.learningapp.auth.AuthTextField
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch
import androidx.compose.ui.res.stringResource
import com.example.learningapp.R

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit

) {
    // Collecting the state from ViewModel
    val state by viewModel.loginState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    // Initialize the CredentialManager securely
    val credentialManager = remember { CredentialManager.create(context) }
    val webClientId = stringResource(id = R.string.default_web_client_id)

    // Google Sign-In logic using the official Credential Manager API
    val onGoogleLoginClick: () -> Unit = {
        coroutineScope.launch {
            try {
                // 1. Instantiate a Google sign-in request
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false) // false allows the user to select any Google account on the device
                    .setServerClientId(webClientId)
                    .build()

                // 2. Create the Credential Manager request
                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                // 3. Launch the native Google Sign-In bottom sheet
                val result = credentialManager.getCredential(
                    request = request,
                    context = context
                )

                // 4. Handle the returned credential
                val credential = result.credential
                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    // Create Google ID Token
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                    // 5. Send the token to the ViewModel to authenticate with Firebase
                    viewModel.loginWithGoogle(googleIdTokenCredential.idToken)
                } else {
                    Log.d("LoginScreen", "Credential is not of type Google ID!")
                }
            } catch (e: GetCredentialException) {
                // This block catches cancellations (user closed the bottom sheet) or real errors
                Log.d("LoginScreen", "Google sign-in failed or was cancelled", e)
            }
        }
    }

    LoginContent(
        state = state,
        onLoginClick = { email, password -> viewModel.login(email, password) },
        onNavigateToRegister = onNavigateToRegister,
        onGoogleLoginClick = onGoogleLoginClick,
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
    onGoogleLoginClick: () -> Unit,
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

        // OR divider
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Text(
                text = "OR",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }

        OutlinedButton(
            onClick = onGoogleLoginClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.google_icon),
                contentDescription = "Google Icon",
                modifier = Modifier.size(20.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Sign in with Google")
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
                onGoogleLoginClick = {},
                onLoginSuccess = {}
            )
        }
    }
}