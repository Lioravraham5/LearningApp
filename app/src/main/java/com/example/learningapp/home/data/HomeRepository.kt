package com.example.learningapp.home.data

import com.example.learningapp.home.models.Category

interface HomeRepository {
    suspend fun getCategories(): List<Category>
}