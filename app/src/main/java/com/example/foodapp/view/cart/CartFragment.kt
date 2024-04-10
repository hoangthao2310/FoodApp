package com.example.foodapp.view.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.OnItemCartClickListener
import com.example.foodapp.OnItemClickListener
import com.example.foodapp.adapter.CartAdapter
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentCartBinding
import com.example.foodapp.model.Cart
import com.example.foodapp.viewmodel.CartViewModel

class CartFragment : BaseFragment<FragmentCartBinding>() {
    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cart: Cart
    override fun getLayout(container: ViewGroup?): FragmentCartBinding =
        FragmentCartBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        cartViewModel.getCart()
        cartViewModel.getCartFirebase.observe(viewLifecycleOwner){listItemCart ->
            val onItemClickListener = object : OnItemCartClickListener{
                override fun onItemIncreaseClick(data: Any?) {

                }

                override fun onItemReduceClick(data: Any?) {}

            }

            cartAdapter = CartAdapter(listItemCart, onItemClickListener)
            binding.rcvCart.adapter = cartAdapter
            binding.progressBarCart.visibility = View.INVISIBLE

        }

        binding.btnBack.setOnClickListener {
            callback.backToPrevious()
        }
    }
}