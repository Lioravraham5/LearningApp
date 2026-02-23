package com.example.learningapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// This trigger Hilt's code generation
@HiltAndroidApp
class MyApplication : Application() {

}