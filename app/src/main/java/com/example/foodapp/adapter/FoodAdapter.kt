package com.example.foodapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.OnItemClickListener
import com.example.foodapp.databinding.ItemBestFoodBinding
import com.example.foodapp.model.Food

class FoodAdapter(
    private var listFood: List<Food>,
    private val itemClick: OnItemClickListener
) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>(){
    inner class FoodViewHolder(private val binding: ItemBestFoodBinding): RecyclerView.ViewHolder(binding.root) {
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
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemBestFoodBinding.inflate(inflater, parent, false)
        return FoodViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listFood.size
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(listFood[position])
    }
}