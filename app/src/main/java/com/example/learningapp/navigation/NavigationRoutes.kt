package com.example.learningapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Defines the high-level nested graphs in the app.
 */
object Graph {
    const val ROOT = "root_graph"
    const val AUTH = "auth_graph"
    const val MAIN = "main_graph"
}

/**
 * Screens inside the Authentication Graph.
 */
sealed class AuthScreen(val route: String) {
    object Login : AuthScreen("login")
    object Register : AuthScreen("register")
}

/**
 * Screens inside the Main Application Graph (Bottom Navigation).
 */
sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "Home", Icons.Outlined.Home)
    object Progress : BottomNavItem("progress", "Progress", Icons.Outlined.EmojiEvents)
    object Profile : BottomNavItem("profile", "Profile", Icons.Outlined.Person)
}