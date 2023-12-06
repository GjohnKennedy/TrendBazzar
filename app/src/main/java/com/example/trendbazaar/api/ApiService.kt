package com.example.trendbazaar.api

import com.example.trendbazaar.model.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("products/categories")
    suspend fun getCategories(): List<String>

    @GET("products/category/{category}")
    suspend fun getProductsForCategory(@Path("category") category: String): ProductResponse

}
