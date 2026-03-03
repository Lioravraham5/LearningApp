package com.example.learningapp.profile

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Delete
import android.provider.Settings
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.learningapp.core.AvatarType
import com.example.learningapp.profile.components.AvatarSelectionSection
import com.example.learningapp.profile.components.DeleteAccountDialog
import com.example.learningapp.profile.components.EditNameDialog
import com.example.learningapp.profile.components.ProfileSettingItem
import com.example.learningapp.profile.components.UserIdentitySection
import androidx.compose.foundation.layout.WindowInsets

/**
 * Stateful entry point for the Profile Screen.
 * Handles the ViewModel, Lifecycle, and Navigation Side-Effects.
 *
 * @param onNavigateToLogin Callback triggered when the user logs out or deletes their account.
 */
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit
) {
    // Collect state in a lifecycle-aware manner
    val state by viewModel.profileState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Handle Navigation Side-Effect: If the user is logged out, navigate away.
    LaunchedEffect(state.isLoggedOut) {
        if (state.isLoggedOut) {
            onNavigateToLogin()
        }
    }

    // Pass data and actions down to the stateless component
    ProfileScreenContent(
        state = state,
        onEditName = { newName -> viewModel.updateDisplayName(newName) },
        onAvatarSelected = { newAvatar -> viewModel.updateAvatar(newAvatar) },
        onLogoutClick = { viewModel.logout() },
        onDeleteAccountClick = { viewModel.deleteAccount() },
        onNotificationSettingsClick = { openAppNotificationSettings(context) },
        onErrorDismissed = { viewModel.clearError() }
    )
}

/**
 * Stateless UI component for the Profile Screen.
 * Purely declarative and highly testable.
 */
@Composable
fun ProfileScreenContent(
    state: ProfileState,
    onEditName: (String) -> Unit,
    onAvatarSelected: (AvatarType) -> Unit,
    onLogoutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onNotificationSettingsClick: () -> Unit,
    onErrorDismissed: () -> Unit
) {
    // Local UI State for Dialogs
    var showEditNameDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Snackbar state for showing errors
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle Error side effect: Show Snackbar when an error occurs
    LaunchedEffect(state.error) {
        state.error?.let { errorMessage ->
            snackbarHostState.showSnackbar(message = errorMessage)
            onErrorDismissed() // Clear the error from the ViewModel state after showing
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    ) { paddingValues ->

        // Main Scrollable Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()) // Enables smooth vertical scrolling
        ) {

            // 1. Header: User Identity
            UserIdentitySection(
                userName = state.userName,
                userEmail = state.userEmail,
                onEditClick = { showEditNameDialog = true }
            )

            // Subtle divider between sections
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )

            // 2. Main Feature: Tutor Selection
            AvatarSelectionSection(
                selectedAvatar = state.selectedAvatar,
                onAvatarSelected = onAvatarSelected
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtle divider before settings
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 3. Settings List
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp) // Spacing between modern settings items
            ) {
                ProfileSettingItem(
                    title = "Notification Settings",
                    icon = Icons.Default.Notifications,
                    onClick = onNotificationSettingsClick
                )

                ProfileSettingItem(
                    title = "Logout",
                    icon = Icons.AutoMirrored.Filled.Logout,
                    onClick = onLogoutClick
                )

                ProfileSettingItem(
                    title = "Delete Account",
                    icon = Icons.Default.Delete,
                    isDestructive = true,
                    onClick = { showDeleteDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(32.dp)) // Bottom padding
        }

        // --- Dialogs ---

        if (showEditNameDialog) {
            EditNameDialog(
                currentName = state.userName,
                onDismiss = { showEditNameDialog = false },
                onConfirm = { newName ->
                    showEditNameDialog = false
                    onEditName(newName)
                }
            )
        }

        if (showDeleteDialog) {
            DeleteAccountDialog(
                onDismiss = { showDeleteDialog = false },
                onConfirm = {
                    showDeleteDialog = false
                    onDeleteAccountClick()
                }
            )
        }

        // --- Full Screen Loading Overlay ---
        if (state.isLoading) {
            // Displays a semi-transparent background with a spinner
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

/**
 * Helper function to launch the Android OS settings screen for this specific app's notifications.
 * This is the Best Practice for managing notifications on Android 8.0+ (API 26+).
 */
private fun openAppNotificationSettings(context: Context) {
    val intent = Intent().apply {
        action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    }
    context.startActivity(intent)
}

// Preview using the Stateless component with mock data
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreenContent(
            state = ProfileState(
                isLoading = false,
                userName = "Student Name",
                userEmail = "student@example.com",
                selectedAvatar = AvatarType.MALE,
                error = null,
                isLoggedOut = false
            ),
            onEditName = {},
            onAvatarSelected = {},
            onLogoutClick = {},
            onDeleteAccountClick = {},
            onNotificationSettingsClick = {},
            onErrorDismissed = {}
        )
    }
}