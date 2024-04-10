package com.example.foodapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.OnItemCartClickListener
import com.example.foodapp.databinding.ItemCartBinding
import com.example.foodapp.model.Cart

class CartAdapter(
    private var listCart: List<Cart>,
    private val itemClick: OnItemCartClickListener
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    inner class CartViewHolder(private val binding: ItemCartBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(cart: Cart) {
            binding.tvNameItem.text = cart.foodName
            binding.tvPriceItem.text = "${cart.price}đ"
            binding.tvQuantity.text = cart.quantity.toString()
            Glide.with(binding.root.context).load(cart.image).into(binding.imgItemCart)

            binding.btnIncreaseQuantity.setOnClickListener {
                itemClick.onItemIncreaseClick(cart)
                val quantity = cart.quantity + 1
                cart.quantity = quantity
                binding.tvQuantity.text = quantity.toString()
            }

            binding.btnReduceQuantity.setOnClickListener {
                itemClick.onItemReduceClick(cart)
                val quantity = cart.quantity - 1
                if (quantity >= 1) {
                    cart.quantity = quantity
                    binding.tvQuantity.text = quantity.toString()
                }
            }
            binding.tvQuantity.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemCartBinding.inflate(inflater, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listCart.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(listCart[position])
    }
}