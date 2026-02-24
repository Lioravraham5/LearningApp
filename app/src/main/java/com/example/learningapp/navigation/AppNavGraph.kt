package com.example.learningapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.learningapp.HomeScreen
import com.example.learningapp.auth.login.LoginScreen
import com.example.learningapp.auth.register.RegisterScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        // --- Login Destination ---
        composable(route = Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginSuccess = {
                    // Navigate to home screen and remove login screen from the back stack
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // --- Register Destination ---
        composable(route = Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    // Go back to the login screen
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    // Navigate to home screen and remove register screen from the back stack
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // --- Home Destination ---
        composable(route = Screen.Home.route) {
            HomeScreen()
        }
    }
}