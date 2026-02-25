package com.example.learningapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.learningapp.home.HomeScreen
import com.example.learningapp.profile.ProfileScreen
import com.example.learningapp.progress.ProgressScreen

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = modifier
    ) {

        // --- Home Tab ---
        composable(BottomNavItem.Home.route) {
            HomeScreen()
        }

        // --- Progress Tab ---
        composable(BottomNavItem.Progress.route) {
            ProgressScreen()
        }

        // --- Profile Tab ---
        composable(BottomNavItem.Profile.route) {
            ProfileScreen()
        }
    }
}