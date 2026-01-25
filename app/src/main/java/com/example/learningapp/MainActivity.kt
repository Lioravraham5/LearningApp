package com.example.learningapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.learningapp.lesson.CategoryScreen
import com.example.learningapp.lesson.LessonScreen
import com.example.learningapp.ui.theme.LearningAppTheme
import androidx.compose.foundation.layout.padding


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LearningAppTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding  ->
                    NavHost(
                        navController = navController,
                        startDestination = "categories",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("categories") {
                            CategoryScreen(
                                onPickTravel = { navController.navigate("lesson_travel") },
                                onPickJobInterview = { navController.navigate("lesson_job") },
                                onPickEverydayConversation = { navController.navigate("lesson_everyday") }
                            )
                        }

                        // For now these all open the same LessonScreen (we can customize later)
                        composable("lesson_travel") { LessonScreen() }
                        composable("lesson_job") { LessonScreen() }
                        composable("lesson_everyday") { LessonScreen() }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    LearningAppTheme {
        CategoryScreen(
            onPickTravel = {},
            onPickJobInterview = {},
            onPickEverydayConversation = {}
        )
    }
}