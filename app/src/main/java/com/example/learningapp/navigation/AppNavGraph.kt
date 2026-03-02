package com.example.learningapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.learningapp.auth.login.LoginScreen
import com.example.learningapp.auth.register.RegisterScreen
import com.example.learningapp.main.MainContainerScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = RootScreen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        // --- Login Destination ---
        composable(route = RootScreen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(RootScreen.Register.route)
                },
                onLoginSuccess = {
                    // Navigate to home screen and remove login screen from the back stack
                    navController.navigate(RootScreen.MainContainer.route) {
                        popUpTo(RootScreen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // --- Register Destination ---
        composable(route = RootScreen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    // Go back to the login screen
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    // Navigate to home screen and remove register screen from the back stack
                    navController.navigate(RootScreen.MainContainer.route) {
                        popUpTo(RootScreen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // --- Home Destination ---
        composable(route = RootScreen.MainContainer.route) {
            MainContainerScreen(
                onLogoutSuccess = {
                    // Navigate to Login and completely clear the backstack!
                    navController.navigate(RootScreen.Login.route) {
                        popUpTo(0) { inclusive = true } // 0 means the absolute root of the nav graph
                    }
                }
            )
        }
    }
}