package com.example.learningapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector


sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "Home", Icons.Outlined.Home)
    object Progress : BottomNavItem("progress", "Progress", Icons.Outlined.EmojiEvents)
    object Profile : BottomNavItem("profile", "Profile", Icons.Outlined.Person)
}