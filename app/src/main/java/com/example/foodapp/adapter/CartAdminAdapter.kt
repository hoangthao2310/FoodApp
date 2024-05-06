package com.example.foodapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.OnItemClickListener
import com.example.foodapp.databinding.ItemCartAdminBinding
import com.example.foodapp.model.CartAdmin

class CartAdminAdapter(
    val listCartAdmin: ArrayList<CartAdmin>,
    var itemClick: OnItemClickListener
): RecyclerView.Adapter<CartAdminAdapter.CartAdminViewHolder>() {

    inner class CartAdminViewHolder(private val binding: ItemCartAdminBinding): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(cartAdmin: CartAdmin){
            binding.tvUserName.text = cartAdmin.userName
            binding.tvQuantityFood.text = "Số lượng món ăn: " + cartAdmin.quantityFood
        }
        init {
            binding.layoutCartAdmin.setOnClickListener{
                itemClick.onItemClick(listCartAdmin[adapterPosition])
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartAdminAdapter.CartAdminViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemCartAdminBinding.inflate(inflater, parent, false)
        return CartAdminViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartAdminAdapter.CartAdminViewHolder, position: Int) {
        holder.bind(listCartAdmin[position])
    }

    override fun getItemCount(): Int {
        return listCartAdmin.size
    }
}