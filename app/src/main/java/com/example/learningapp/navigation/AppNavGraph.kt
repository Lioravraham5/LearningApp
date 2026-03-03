package com.example.learningapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.learningapp.auth.login.LoginScreen
import com.example.learningapp.auth.register.RegisterScreen
import androidx.navigation.navigation
import com.example.learningapp.home.HomeScreen
import com.example.learningapp.profile.ProfileScreen
import com.example.learningapp.progress.ProgressScreen

/**
 * The single, unified Navigation Graph for the entire application.
 * Uses nested graphs to separate the Authentication flow from the Main app flow.
 */
@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Graph.AUTH, // The app always starts by checking the Auth area
        route = Graph.ROOT, // Optional: Gives a name to the entire tree
        modifier = modifier
    ) {

        // ==========================================
        // REGION 1: Authentication Graph
        // ==========================================
        navigation(
            route = Graph.AUTH,
            startDestination = AuthScreen.Login.route
        ) {
            composable(route = AuthScreen.Login.route) {
                LoginScreen(
                    onNavigateToRegister = {
                        navController.navigate(AuthScreen.Register.route)
                    },
                    onLoginSuccess = {
                        // Success! Navigate to the MAIN graph and destroy the AUTH history
                        navController.navigate(Graph.MAIN) {
                            popUpTo(Graph.AUTH) { inclusive = true }
                        }
                    }
                )
            }

            composable(route = AuthScreen.Register.route) {
                RegisterScreen(
                    onNavigateToLogin = {
                        navController.popBackStack()
                    },
                    onRegisterSuccess = {
                        // Success! Navigate to the MAIN graph and destroy the AUTH history
                        navController.navigate(Graph.MAIN) {
                            popUpTo(Graph.AUTH) { inclusive = true }
                        }
                    }
                )
            }
        }

        // ==========================================
        // REGION 2: Main Application Graph
        // ==========================================
        navigation(
            route = Graph.MAIN,
            startDestination = BottomNavItem.Home.route
        ) {
            composable(route = BottomNavItem.Home.route) {
                HomeScreen()
            }

            composable(route = BottomNavItem.Progress.route) {
                ProgressScreen()
            }

            composable(route = BottomNavItem.Profile.route) {
                ProfileScreen(
                    onNavigateToLogin = {
                        navController.navigate(Graph.AUTH) {
                            popUpTo(Graph.MAIN) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}