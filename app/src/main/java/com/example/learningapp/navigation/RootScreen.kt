package com.example.learningapp.navigation

sealed class RootScreen(val route: String) {
    object Login : RootScreen("login")
    object Register : RootScreen("register")
    object MainContainer : RootScreen("main_container")
}