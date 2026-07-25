package com.example.learningapp.categoryDetails.di

import com.example.learningapp.categoryDetails.data.CategoryDetailsRepository
import com.example.learningapp.categoryDetails.data.RemoteCategoryDetailsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt module that tells the dependency injection framework how to provide
 * instances related to the Category Details feature.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class CategoryDetailsModule {

    /**
     * Binds the interface to our mock implementation.
     * When ready for production, just change the parameter to your RemoteImpl class.
     */
    @Binds
    abstract fun bindCategoryDetailsRepository(
        impl: RemoteCategoryDetailsRepositoryImpl
    ): CategoryDetailsRepository
}