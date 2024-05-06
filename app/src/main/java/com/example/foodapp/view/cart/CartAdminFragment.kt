package com.example.foodapp.view.cart

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.OnItemClickListener
import com.example.foodapp.adapter.CartAdminAdapter
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentCartAdminBinding
import com.example.foodapp.model.CartAdmin
import com.example.foodapp.viewmodel.CartViewModel

class CartAdminFragment : BaseFragment<FragmentCartAdminBinding>() {
    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartAdminAdapter: CartAdminAdapter
    override fun getLayout(container: ViewGroup?): FragmentCartAdminBinding =
        FragmentCartAdminBinding.inflate(layoutInflater, container, false)
    override fun initViews() {
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]

        cartViewModel.getCartAdmin()
        cartViewModel.getCartAdmin.observe(viewLifecycleOwner){list ->
            val onItemClickListener = object : OnItemClickListener{
                override fun onItemClick(data: Any?) {
                    val cartAdmin = data as CartAdmin
                    callback.showFragment(CartAdminFragment::class.java, CartFragment::class.java, 0, 0, cartAdmin, true)
                }

                override fun onItemAddClick(data: Any?) {}

                override fun onItemEditClick(data: Any?) {}

                override fun onItemDeleteClick(data: Any?) {}
            }
            cartAdminAdapter = CartAdminAdapter(list, onItemClickListener)
            binding.rcvCartAdmin.adapter = cartAdminAdapter
            binding.progressBar.visibility = View.INVISIBLE
        }

        binding.btnBack.setOnClickListener {
            callback.backToPrevious()
        }
    }
}