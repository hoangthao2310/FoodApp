package com.example.foodapp.view.cart

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.OnItemCartClickListener
import com.example.foodapp.adapter.CartAdapter
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentCartBinding
import com.example.foodapp.model.Cart
import com.example.foodapp.model.User
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
                    cart = data as Cart
                    cartViewModel.updateCart(cart)
                    cartViewModel.totalPrice()
                    cartViewModel.totalPrice.observe(viewLifecycleOwner){
                        binding.tvTotalPrice.text = it.toString()
                    }
                }

                override fun onItemReduceClick(data: Any?) {
                    cart = data as Cart
                    cartViewModel.updateCart(cart)
                    cartViewModel.totalPrice()
                    cartViewModel.totalPrice.observe(viewLifecycleOwner){
                        binding.tvTotalPrice.text = it.toString()
                    }
                }

            }

            if(listItemCart == null){
                binding.layoutButtonIntoMoney.visibility = View.INVISIBLE
                binding.progressBarCart.visibility = View.INVISIBLE
            }

            cartAdapter = CartAdapter(listItemCart, onItemClickListener)
            binding.rcvCart.adapter = cartAdapter
            binding.progressBarCart.visibility = View.INVISIBLE
        }

        cartViewModel.totalPrice()
        cartViewModel.totalPrice.observe(viewLifecycleOwner){
            binding.tvTotalPrice.text = it.toString()
        }

        binding.btnOrder.setOnClickListener {
            cartViewModel.totalPrice.observe(viewLifecycleOwner){
                cartViewModel.order(it)
                cartViewModel.isCheck.observe(viewLifecycleOwner){isCheck ->
                    if(isCheck){
                        callback.showFragment(CartFragment::class.java, OrderFragment::class.java, 0, 0, null, true)
                    }
                }
            }
        }

        binding.btnBack.setOnClickListener {
            callback.backToPrevious()
        }
    }
}