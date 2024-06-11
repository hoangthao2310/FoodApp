package com.example.foodapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.OnItemCartClickListener
import com.example.foodapp.databinding.ItemCartBinding
import com.example.foodapp.databinding.ItemOrderBinding
import com.example.foodapp.model.Cart

class OrderAdapter(
    private var listCart: List<Cart>,
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    inner class OrderViewHolder(private val binding: ItemOrderBinding): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(cart: Cart) {
            binding.tvNameItem.text = cart.foodName
            binding.tvPriceItem.text = "${cart.price}đ"
            binding.tvQuantityItem.text = "Số lượng: " + cart.quantity.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemOrderBinding.inflate(inflater, parent, false)
        return OrderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listCart.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(listCart[position])
    }

}