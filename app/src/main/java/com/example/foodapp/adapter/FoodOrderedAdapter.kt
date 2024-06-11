package com.example.foodapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.databinding.ItemOrderBinding
import com.example.foodapp.model.Cart
import com.example.foodapp.model.FoodOrdered

class FoodOrderedAdapter(
    private var listFood: List<FoodOrdered>,
) : RecyclerView.Adapter<FoodOrderedAdapter.FoodOrderedViewHolder>() {
    inner class FoodOrderedViewHolder(private val binding: ItemOrderBinding): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(foodOrdered: FoodOrdered) {
            binding.tvNameItem.text = foodOrdered.foodName
            binding.tvPriceItem.text = "${foodOrdered.price}đ"
            binding.tvQuantityItem.text = "Số lượng: " + foodOrdered.quantity.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodOrderedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemOrderBinding.inflate(inflater, parent, false)
        return FoodOrderedViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listFood.size
    }

    override fun onBindViewHolder(holder: FoodOrderedViewHolder, position: Int) {
        holder.bind(listFood[position])
    }
}