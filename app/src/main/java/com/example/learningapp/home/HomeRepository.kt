package com.example.learningapp.home

interface HomeRepository {
    suspend fun getCategories(): List<Category>
}