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
import com.example.learningapp.lessonEnd.LessonEndScreen
import com.example.learningapp.lessonProgress.LessonProgressScreen
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
                    onNavigateToLessonPlayer = { id, startIndex ->
                        navController.navigate(MainScreen.LessonProgress.createRoute(id, startIndex))
                    }
                )
            }

            composable(
                route = MainScreen.LessonProgress.route,
                arguments = listOf(
                    navArgument("lessonId") {
                        type = NavType.StringType
                    },
                    navArgument("startIndex") {
                        type = NavType.IntType
                        defaultValue = 0
                    }
                )
            ) { navBackStackEntry ->
                // BEST PRACTICE: Safely extract the lessonId from the navigation arguments
                val lessonId = navBackStackEntry.arguments?.getString("lessonId") ?: return@composable

                LessonProgressScreen(
                    lessonId = lessonId,
                    onExitLesson = {
                        // When the user clicks the 'X' button and confirms exit,
                        // we simply pop the back stack to return to the Lesson Details screen.
                        navController.popBackStack()
                    },
                    onNavigateToLessonEnd = { completedLessonId, runId ->
                        // Navigate to the Summary screen AND destroy the Progress screen behind it.
                        // This prevents the user from pressing "Back" and accidentally re-entering the finished lesson.
                        navController.navigate(MainScreen.LessonEnd.createRoute(completedLessonId, runId)) {
                            popUpTo(MainScreen.LessonProgress.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(
                route = MainScreen.LessonEnd.route,
                arguments = listOf(
                    navArgument("lessonId") { type = NavType.StringType },
                    navArgument("runId") { type = NavType.StringType }
                )
            ) {
                LessonEndScreen(
                    onNavigateToCategoryDetails = {
                        // The user clicked "Continue".
                        // Stack is currently: CategoryDetails -> LessonDetails -> LessonEnd.
                        // We pop up to LessonDetails and remove it (inclusive = true), landing cleanly on CategoryDetails!
                        navController.popBackStack(MainScreen.LessonDetails.route, inclusive = true)
                    },
                    onPracticeAgain = { lessonIdToRepeat ->
                        // The user clicked "Practice Again".
                        // Replace the End screen with a fresh Progress screen starting at index 0.
                        navController.navigate(MainScreen.LessonProgress.createRoute(lessonIdToRepeat, startIndex = 0)) {
                            popUpTo(MainScreen.LessonEnd.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}