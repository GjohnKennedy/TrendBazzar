package com.example.trendbazaar.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trendbazaar.utils.MyResult
import com.example.trendbazaar.R
import com.example.trendbazaar.adapter.CategoryAdapter
import com.example.trendbazaar.adapter.ProductAdapter
import com.example.trendbazaar.adapter.SpacesItemDecoration
import com.example.trendbazaar.databinding.ActivityMainBinding
import com.example.trendbazaar.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var categoryAdapter: CategoryAdapter

    @Inject
    lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.item_spacing)


        binding.horizontalRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
            addItemDecoration(SpacesItemDecoration( spacingInPixels))
        }

        binding.verticalRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = productAdapter
        }


        binding.switch1.setOnCheckedChangeListener { _, isChecked ->

            if (!isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

        productAdapter.setOnItemClickListener { action ->
            viewModel.deleteProduct(action)
            Log.e("DELETE",action.toString())

        }

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filterProducts(newText.orEmpty())
                return true
            }
        })

        viewModel.filteredProducts.observe(this) { result ->
            when (result) {
                is MyResult.Success -> {
                    productAdapter.differ.submitList(result.data)
                    hideProgressBar()
                }
                is MyResult.Error -> {
                    Toast.makeText(this, "Failed to filter products", Toast.LENGTH_SHORT).show()
                    hideProgressBar()
                }
                is MyResult.Loading ->{
                    showProgressBar()
                }

                else -> {Log.e("Check","demo")}
            }
        }

        categoryAdapter.setOnItemClickListener { selectedCategory ->

            viewModel.fetchProductsForCategory(selectedCategory)
        }

        viewModel.categories.observe(this) { result ->
            when (result) {
                is MyResult.Success -> {
                    categoryAdapter.differ.submitList(result.data)
                    hideProgressBar()
                }

                is MyResult.Error -> {
                    Toast.makeText(this, "Failed to fetch categories", Toast.LENGTH_SHORT).show()
                    hideProgressBar()
                }
                is MyResult.Loading -> {
                    showProgressBar()
                }
                else -> {Log.e("Check","demo")}

            }
        }

        viewModel.products.observe(this) { result ->
            when (result) {
                is MyResult.Success -> {
                    productAdapter.differ.submitList(result.data)
                    hideProgressBar()
                }

                is MyResult.Error -> {
                    Toast.makeText(this, "Failed to fetch products", Toast.LENGTH_SHORT).show()
                    showProgressBar()

                }
                is MyResult.Loading -> {
                    showProgressBar()
                }
                else -> {Log.e("Check","$result")}

            }
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }
}

