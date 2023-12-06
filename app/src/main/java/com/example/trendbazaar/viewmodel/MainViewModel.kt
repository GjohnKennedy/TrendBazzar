package com.example.trendbazaar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.trendbazaar.utils.MyResult
import com.example.trendbazaar.model.Product
import com.example.trendbazaar.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _categories = MutableLiveData<MyResult<List<String>>>()
    val categories: LiveData<MyResult<List<String>>> get() = _categories

    private val _products = MutableLiveData<MyResult<List<Product>>>()
    val products: LiveData<MyResult<List<Product>>> get() = _products

    private val _filteredProducts = MutableLiveData<MyResult<List<Product>>>()
    val filteredProducts: LiveData<MyResult<List<Product>>> get() = _filteredProducts

    private var lastFetchedCategory: String? = null

    init {
        lastFetchedCategory?.let { category ->
            fetchProductsForCategory(category)
        } ?: fetchProductsForCategory("smartphones")

        fetchCategories()
    }

    private fun fetchCategories() = viewModelScope.launch {
        _categories.postValue(MyResult.Loading)
        try {
            val response = repository.getCategories()
            _categories.postValue(MyResult.Success(response))
        } catch (e: Exception) {
            _categories.postValue(MyResult.Error(e))
        }
    }

    fun fetchProductsForCategory(category: String) {
        viewModelScope.launch {
            _products.postValue(MyResult.Loading)
            try {
                val response = repository.getProductsForCategory(category)
                _products.postValue(MyResult.Success(response))
            } catch (e: Exception) {
                _products.postValue(MyResult.Error(e))
            }

            if (category != "smartphones") {
                lastFetchedCategory = category
            }
        }
    }

    fun filterProducts(query: String) {
        viewModelScope.launch {
            try {
                val currentProductsResult = _products.value
                if (currentProductsResult is MyResult.Success) {
                    val allProducts = currentProductsResult.data

                    val filteredList = allProducts.filter { product ->
                        product.title.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))
                    }

                    _filteredProducts.value = MyResult.Success(filteredList)
                } else {
                    _filteredProducts.value = MyResult.Error(Exception("Failed to get current products"))
                }
            } catch (e: Exception) {
                _filteredProducts.value = MyResult.Error(e)
            }
        }
    }

    fun deleteProduct(productId: Int) {
        viewModelScope.launch {
            try {
                val currentProductsResult = _products.value
                if (currentProductsResult is MyResult.Success) {
                    val currentProducts = currentProductsResult.data.toMutableList()

                    val index = currentProducts.indexOfFirst { it.id == productId }

                    if (index != -1) {
                        currentProducts.removeAt(index)

                        _products.value = MyResult.Success(currentProducts)
                    }
                } else {
                    _products.value = MyResult.Error(Exception("Failed to get current products"))
                }
            } catch (e: Exception) {
                _products.value = MyResult.Error(e)
            }
        }
    }



}


