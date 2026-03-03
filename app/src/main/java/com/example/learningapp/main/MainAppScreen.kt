package com.example.learningapp.main

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.learningapp.navigation.BottomNavItem
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.learningapp.navigation.AppNavGraph

/**
 * The Root Composable of the App.
 * Manages the SINGLE NavController and the conditional Bottom Navigation Bar.
 */
@Composable
fun MainAppScreen() {
    // 1. The Single NavController for the entire app!
    val navController = rememberNavController()

    // 2. Observe the current route to determine UI state
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    // 3. Define which screens should show the Bottom Bar
    val bottomBarRoutes = listOf(
        BottomNavItem.Home.route,
        BottomNavItem.Progress.route,
        BottomNavItem.Profile.route
    )
    val shouldShowBottomBar = currentRoute in bottomBarRoutes

    Scaffold(
        bottomBar = {
            // 4. Conditional Rendering: Only draw the bar if we are in the Main Graph
            if (shouldShowBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.background
                    //windowInsets = WindowInsets(0.dp) // Removes extra bottom padding
                ) {
                    val items = listOf(
                        BottomNavItem.Home,
                        BottomNavItem.Progress,
                        BottomNavItem.Profile
                    )

                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            // Check if the current route matches this item
                            selected = currentRoute == screen.route,
                            onClick = {
                                if (currentRoute != screen.route) {
                                    // Standard M3 Bottom Navigation behavior
                                    navController.navigate(screen.route) {
                                        // Pop up to the start destination of the graph to
                                        // avoid building up a large stack of destinations
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        // Avoid multiple copies of the same destination
                                        launchSingleTop = true
                                        // Restore state when reselecting a previously selected item
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->

        // 5. Host the unified Navigation Graph here, passing the padding from the Scaffold
        AppNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}