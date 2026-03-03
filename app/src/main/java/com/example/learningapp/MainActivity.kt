package com.example.learningapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.learningapp.ui.theme.LearningAppTheme
import com.example.learningapp.main.MainAppScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LearningAppTheme {
                // The entire app's UI, Navigation, and Scaffold are now
                // perfectly encapsulated and managed inside MainAppScreen!
                MainAppScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    LearningAppTheme {
        MainActivity()
    }
}