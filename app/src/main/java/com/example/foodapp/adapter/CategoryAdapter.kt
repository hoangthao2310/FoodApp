package com.example.foodapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.OnItemClickListener
import com.example.foodapp.R
import com.example.foodapp.databinding.ItemCategoryBinding
import com.example.foodapp.model.Category

class CategoryAdapter(
    private var listCategory: List<Category>,
    private val itemClick: OnItemClickListener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){

    inner class CategoryViewHolder(private var binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(category: Category, position: Int){
            binding.tvCategoryName.text = category.categoryName
            when(position){
                0 -> binding.imgCategory.setBackgroundResource(R.drawable.cart_1_background)
                1 -> binding.imgCategory.setBackgroundResource(R.drawable.cart_2_background)
                2 -> binding.imgCategory.setBackgroundResource(R.drawable.cart_3_background)
                3 -> binding.imgCategory.setBackgroundResource(R.drawable.cart_4_background)
                4 -> binding.imgCategory.setBackgroundResource(R.drawable.cart_5_background)
                5 -> binding.imgCategory.setBackgroundResource(R.drawable.cart_6_background)
                6 -> binding.imgCategory.setBackgroundResource(R.drawable.cart_7_background)
                7 -> binding.imgCategory.setBackgroundResource(R.drawable.cart_8_background)
            }
            Glide.with(itemView.context).load(category.image).into(binding.imgCategory)
        }

        init {
            binding.imgCategory.setOnClickListener {
                itemClick.onItemClick(listCategory[adapterPosition])
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryAdapter.CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemCategoryBinding.inflate(inflater, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.CategoryViewHolder, position: Int) {
        holder.bind(listCategory[position], position)
    }

    override fun getItemCount(): Int {
        return listCategory.size
    }

}