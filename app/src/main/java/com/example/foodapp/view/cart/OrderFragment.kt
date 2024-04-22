package com.example.foodapp.view.cart

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.adapter.OrderAdapter
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentOrderBinding
import com.example.foodapp.model.Location
import com.example.foodapp.view.profile.location.SavedLocationFragment
import com.example.foodapp.viewmodel.CartViewModel
import com.example.foodapp.viewmodel.AccountViewModel
import com.google.firebase.auth.FirebaseUser

class OrderFragment : BaseFragment<FragmentOrderBinding>() {
    private lateinit var cartViewModel: CartViewModel
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var location: Location
    override fun getLayout(container: ViewGroup?): FragmentOrderBinding =
        FragmentOrderBinding.inflate(layoutInflater, container, false)

    @SuppressLint("SetTextI18n")
    override fun initViews() {
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        location = data as Location
        if(location != null){
            binding.tvUserName.text = location.contactPersonName
            binding.tvPhoneNumber.text = location.contactPhoneNumber
            binding.tvAddress.text = location.address
            binding.tvSelectAddressOrder.text = ""
        }


        binding.layoutInfoUserOrder.setOnClickListener {
            callback.showFragment(OrderFragment::class.java, SavedLocationFragment::class.java, 0, 0)
        }

        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        cartViewModel.getCart()
        cartViewModel.getCartFirebase.observe(viewLifecycleOwner){
            orderAdapter = OrderAdapter(it)
            binding.rcvOrder.adapter = orderAdapter
            binding.proBarOrder.visibility = View.INVISIBLE
        }

        cartViewModel.totalPrice()
        cartViewModel.totalPrice.observe(viewLifecycleOwner){
            binding.tvTotalFood.text = it.toString() + "đ"
            binding.tvTotalOrder.text = it.toString() + "đ"
            binding.tvTotalPrice.text = it.toString() + "đ"
        }
        binding.btnConfirmOrder.setOnClickListener {
            notify("Đặt hàng thành công")
        }

        binding.btnBack.setOnClickListener {
            callback.backToPrevious()
        }
    }

}