package com.example.foodapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.OnItemClickListener
import com.example.foodapp.databinding.ItemFoodBinding
import com.example.foodapp.model.Food

class FoodAdapter(
    private var listFood: List<Food>,
    private val itemClick: OnItemClickListener
) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>(){
    @SuppressLint("NotifyDataSetChanged")
    fun setFilteredList(listFood: List<Food>){
        this.listFood = listFood
        notifyDataSetChanged()
    }
    inner class FoodViewHolder(private val binding: ItemFoodBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(food: Food){
            binding.tvFoodName.text = food.foodName
            binding.tvPrice.text = "${food.price}đ"
            binding.tvRating.text = food.rating.toString()
            binding.tvTime.text = food.time
            Glide.with(itemView.context).load(food.image).into(binding.imgFood)
        }

        init {
            binding.layoutItemFood.setOnClickListener {
                itemClick.onItemClick(listFood[adapterPosition])
            }
            binding.btnAddFood.setOnClickListener {
                itemClick.onItemAddClick(listFood[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemFoodBinding.inflate(inflater, parent, false)
        return FoodViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listFood.size
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(listFood[position])
    }
}