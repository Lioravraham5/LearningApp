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
 * Screens inside the Main Application Graph that are NOT on the Bottom Navigation Bar.
 */
sealed class MainScreen(val route: String) {
    // The route includes a placeholder parameter: {categoryId}
    object CategoryDetails : MainScreen("category_details/{categoryId}") {

        /**
         * A helper function to easily create the route string when we want to navigate.
         * Usage: MainScreen.CategoryDetails.createRoute("1") -> returns "category_details/1"
         */
        fun createRoute(categoryId: String): String {
            return "category_details/$categoryId"
        }
    }

    // The route includes a placeholder parameter: {lessonId}
    object LessonDetails : MainScreen("lesson_details/{lessonId}") {
        /**
         * A helper function to easily create the route string when we want to navigate.
         * Usage: MainScreen.LessonDetails.createRoute("1") -> returns "lesson_details/1"
         */
        fun createRoute(lessonId: String): String {
            return "lesson_details/$lessonId"
        }
    }

    // The Interactive Lesson Player, It includes a placeholder parameter: {lessonId} and an optional {startIndex}
    object LessonProgress : MainScreen("lesson_progress/{lessonId}?startIndex={startIndex}") {
        /**
         * A helper function to easily create the route string when we want to navigate.
         * Usage: MainScreen.LessonProgress.createRoute("123", 2) -> returns "lesson_progress/123?startIndex=2"
         */
        fun createRoute(lessonId: String, startIndex: Int = 0): String {
            return "lesson_progress/$lessonId?startIndex=$startIndex"
        }
    }

    object LessonEnd : MainScreen("lesson_end/{lessonId}/{runId}") {
        /**
         * A helper function to easily create the route string when we want to navigate.
         * Usage: MainScreen.LessonEnd.createRoute("123", "uuid-456")
         */
        fun createRoute(lessonId: String, runId: String): String {
            return "lesson_end/$lessonId/$runId"
        }
    }
}

/**
 * Screens inside the Main Application Graph (Bottom Navigation).
 */
sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "Home", Icons.Outlined.Home)
    object Progress : BottomNavItem("progress", "Progress", Icons.Outlined.EmojiEvents)
    object Profile : BottomNavItem("profile", "Profile", Icons.Outlined.Person)
}