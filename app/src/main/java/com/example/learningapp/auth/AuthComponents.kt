package com.example.learningapp.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Composable
fun AuthPasswordTextField(
    value: String, // The current text in the text field
    onValueChange: (String) -> Unit,
    label: String, // The placeholder text
    modifier: Modifier = Modifier
) {

    // State to track whether the password should be shown as plain text or hidden as dots.
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        // Transforms the text visibility based on the 'passwordVisible' state: None = plain text, PasswordVisualTransformation = black dots.
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        // Configures the software keyboard behavior
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password, // Shows a password-optimized keyboard
            autoCorrectEnabled = false, // Disables autocorrect to prevent accidental changes
            imeAction = ImeAction.Done // Changes the 'enter' key to a 'Done' action
        ),
        singleLine = true, // Prevents the user from adding new lines

        // Add a trailing icon (eye icon) to toggle password visibility
        trailingIcon = {
            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            // Accessibility description for screen readers
            val description = if (passwordVisible) "Hide password" else "Show password"

            // Button wrapper around the icon to make it clickable
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = description)
            }
        }
    )
}