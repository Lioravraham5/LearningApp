package com.example.learningapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.learningapp.auth.login.LoginScreen
import com.example.learningapp.auth.register.RegisterScreen
import androidx.navigation.navigation
import com.example.learningapp.categoryDetails.CategoryDetailsScreen
import com.example.learningapp.home.HomeScreen
import com.example.learningapp.lessonDetails.LessonDetailsScreen
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
                HomeScreen(
                    onNavigateToCategory = { categoryId ->
                        // When a category is clicked, navigate to the Details screen with its ID
                        navController.navigate(MainScreen.CategoryDetails.createRoute(categoryId))
                    }
                )
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

            composable(
                route = MainScreen.CategoryDetails.route,
                // We tell the NavGraph to expect a String parameter named "categoryId"
                arguments = listOf(
                    navArgument("categoryId") {
                        type = NavType.StringType
                    }
                )
            ) {
                CategoryDetailsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onNavigateToLesson = { lessonId ->
                        navController.navigate(MainScreen.LessonDetails.createRoute(lessonId))
                    }
                )
            }

            composable(
                route = MainScreen.LessonDetails.route,
                // Tell the NavGraph to expect a String parameter named "lessonId"
                arguments = listOf(
                    navArgument("lessonId") {
                        type = NavType.StringType
                    }
                )
            ) { navBackStackEntry ->
                // Extract the parameter from the arguments just for the placeholder
                // BEST PRACTICE: In the final version, the ViewModel will extract this via SavedStateHandle.
                val lessonId = navBackStackEntry.arguments?.getString("lessonId")

                LessonDetailsScreen(
                    onBackClick = {
                        // Standard back navigation
                        navController.popBackStack()
                    },
                    onNavigateToLessonPlayer = { lessonId, startIndex ->
                        // TODO: We will add the actual navigation logic here next!
                    }
                )
            }
        }
    }
}