package com.example.trendbazaar.repository

import com.example.trendbazaar.api.ApiService
import com.example.trendbazaar.model.Product
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getCategories(): List<String> {
        return try {
            apiService.getCategories()
        } catch (e: Exception) {
            throw e
        }
    }


    suspend fun getProductsForCategory(category: String): List<Product> {
        return try {
            apiService.getProductsForCategory(category).products
        } catch (e: Exception) {
            throw e
        }
    }

}
