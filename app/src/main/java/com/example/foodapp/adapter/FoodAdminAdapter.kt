package com.example.foodapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.OnItemClickListener
import com.example.foodapp.databinding.ItemFavouriteFoodBinding
import com.example.foodapp.model.Food

class FoodAdminAdapter(
    var listFood: ArrayList<Food>,
    private val itemClick: OnItemClickListener
): RecyclerView.Adapter<FoodAdminAdapter.FoodAdminViewHolder>() {

    inner class FoodAdminViewHolder(private val binding: ItemFavouriteFoodBinding): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(food: Food){
            binding.tvFoodName.text = food.foodName
            binding.tvPrice.text = food.price.toString() + "đ"
            binding.tvRating.text = food.rating.toString()
            binding.tvTime.text = food.time
            Glide.with(itemView.context).load(food.image).into(binding.imgFood)
        }

        init {
            binding.layoutItemFood.setOnClickListener {
                itemClick.onItemClick(listFood[adapterPosition])
            }
            binding.btnDelete.setOnClickListener {
                itemClick.onItemDeleteClick(listFood[adapterPosition])
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FoodAdminAdapter.FoodAdminViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemFavouriteFoodBinding.inflate(inflater, parent, false)
        return FoodAdminViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: FoodAdminAdapter.FoodAdminViewHolder,
        position: Int
    ) {
        holder.bind(listFood[position])
    }

    override fun getItemCount(): Int {
        return listFood.size
    }
    fun removeItem(position: Int) {
        listFood.removeAt(position)
        notifyItemRemoved(position)
    }
}