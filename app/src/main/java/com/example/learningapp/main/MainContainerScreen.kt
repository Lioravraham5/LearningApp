package com.example.learningapp.main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.learningapp.navigation.BottomNavItem
import com.example.learningapp.navigation.BottomNavGraph

/**
 * Main container screen displayed after authentication.
 *
 * This composable is responsible for:
 * - Rendering the Bottom Navigation bar
 * - Hosting the internal navigation graph (MainNavGraph)
 *
 */
@Composable
fun MainContainerScreen(
    onLogoutSuccess: () -> Unit
) {

    // Internal NavController dedicated to bottom navigation.
    // This is separate from the root NavController used in MainActivity.
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background,
                // Cancel automatic extra padding at the bottom of the screen.
                windowInsets = WindowInsets(0.dp)
            ) {

                // Observe the current back stack entry as State so the selected tab updates automatically when navigation changes.
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                // Define the bottom navigation items.
                val items = listOf(
                    BottomNavItem.Home,
                    BottomNavItem.Progress,
                    BottomNavItem.Profile
                )

                items.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                screen.icon,
                                contentDescription = screen.title
                            )
                        },
                        label = {
                            Text(screen.title)
                        },

                        // Mark the item as selected if its route matches the current destination.
                        selected = currentDestination
                            ?.hierarchy
                            ?.any { it.route == screen.route } == true,

                        onClick = {
                            bottomNavController.navigate(screen.route) {

                                // Ensures classic Bottom Navigation behavior:
                                // Avoid building up a large back stack when switching tabs.
                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }

                                // Avoid multiple copies of the same destination.
                                launchSingleTop = true

                                // Restore previously saved state when returning to a tab.
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->

        // Attach the bottom navigation graph to this container using the bottomNavController.
        BottomNavGraph(
            navController = bottomNavController,
            onLogoutSuccess = onLogoutSuccess,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainContainerScreenPreview() {
    MaterialTheme {
        MainContainerScreen(
            onLogoutSuccess = {}
        )
    }
}