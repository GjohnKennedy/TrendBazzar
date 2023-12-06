package com.example.trendbazaar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trendbazaar.R
import com.example.trendbazaar.databinding.ProductsLayoutBinding
import com.example.trendbazaar.model.Product
import javax.inject.Inject

class ProductAdapter @Inject constructor() : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ProductsLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ViewHolder(private val binding: ProductsLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product) {

            binding.apply {
                productTitle.text = item.title
                productDescription.text=item.description
                productPrice.text="₹ ${item.price.toString()}"
                productDiscount.text= "${item.discountPercentage.toString()}% OFF"
                productStock.text="Stock - ${item.stock.toString()}"
                productBrand.text=item.brand

                Glide.with(itemView.context)
                    .load(item.thumbnail)
                    .error(R.drawable.ic_img)
                    .placeholder(R.drawable.ic_img)
                    .into(productImage)


                ratingBar.rating=item.rating.toFloat()

                deleteIcon.setOnClickListener {
                    onItemClickListener?.invoke(item.id)

                }

            }
        }
    }

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    private val differCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

}
