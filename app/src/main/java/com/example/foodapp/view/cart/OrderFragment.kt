package com.example.foodapp.view.cart

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.R
import com.example.foodapp.adapter.OrderAdapter
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentOrderBinding
import com.example.foodapp.model.Order
import com.example.foodapp.view.home.HomeUserFragment
import com.example.foodapp.view.cart.dialog.OrderSuccessDialog
import com.example.foodapp.view.cart.dialog.OnClickListener
import com.example.foodapp.viewmodel.CartViewModel
import com.example.foodapp.viewmodel.AccountViewModel
import com.google.firebase.auth.FirebaseUser


class OrderFragment : BaseFragment<FragmentOrderBinding>() {
    private lateinit var cartViewModel: CartViewModel
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var firebaseUser: FirebaseUser

    private var delivery: String? = null
    private var payment: String? = null
    private var total: Double? = null
    private var describeOrder: String? = null
    override fun getLayout(container: ViewGroup?): FragmentOrderBinding =
        FragmentOrderBinding.inflate(layoutInflater, container, false)

    @SuppressLint("SetTextI18n")
    override fun initViews() {
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        firebaseUser = data as FirebaseUser
        accountViewModel.getUserDetail(firebaseUser.uid)
        accountViewModel.getUser.observe(viewLifecycleOwner){user ->
            binding.tvUserName.text = user.contactPersonName
            binding.tvPhoneNumber.text = user.contactPhoneNumber
            binding.tvAddress.text = user.address

            if(binding.tvAddress.text.isEmpty()){
                binding.tvSelectAddressOrder.visibility = View.VISIBLE
            }
        }

        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        //cartViewModel.getCart()
        cartViewModel.getCartFirebase.observe(viewLifecycleOwner){
            orderAdapter = OrderAdapter(it)
            binding.rcvOrder.adapter = orderAdapter
            binding.proBarOrder.visibility = View.INVISIBLE
            for(cart in it){
                describeOrder += cart.foodName + ": Giá: " + cart.price + ", Số lượng: " + cart.quantity + "\n"
            }
        }

        //cartViewModel.totalPrice()
        cartViewModel.totalPrice.observe(viewLifecycleOwner){
            binding.tvTotalFood.text = it.toString() + "đ"
            total = it + 10000.0
            binding.tvTotalPrice.text = total.toString() + "đ"
            binding.tvTotalOrder.text = total.toString() + "đ"

            binding.radFastDelivery.setOnCheckedChangeListener{ _, isChecked ->
                if (isChecked){
                    binding.tvTransportFee.text = "10000.0đ"
                    total = it + 10000.0 - 10000.0
                    binding.tvTotalPrice.text = total.toString() + "đ"
                    binding.tvTotalOrder.text = total.toString() + "đ"
                }
            }

            binding.radExpressDelivery.setOnCheckedChangeListener{ _, isChecked ->
                if (isChecked){
                    binding.tvTransportFee.text = "15000.0đ"
                    total = it + 15000.0 - 10000.0
                    binding.tvTotalPrice.text = total.toString() + "đ"
                    binding.tvTotalOrder.text = total.toString() + "đ"
                }
            }
        }

        binding.btnConfirmOrder.setOnClickListener {
            if(binding.radFastDelivery.isChecked){
                delivery = getString(R.string.fast_delivery)
            }else if(binding.radExpressDelivery.isChecked){
                delivery = getString(R.string.express_delivery)
            }else{
                binding.radFastDelivery.error = "Vui lòng chọn phương thức giao hàng"
            }

            if(binding.radCash.isChecked){
                payment = getString(R.string.cash)
            }else if(binding.radElecWallet.isChecked){
                payment = getString(R.string.elecwallet)
            }else{
                binding.radCash.error = "Vui lòng chọn phương thức thanh toán"
            }

            if(delivery != null && payment != null){
                val order = Order(
                    orderId = "",
                    userName = binding.tvUserName.text.toString(),
                    phoneNumber = binding.tvPhoneNumber.text.toString(),
                    totalPrice = total,
                    address = binding.tvAddress.text.toString(),
                    deliveryMethods = delivery,
                    paymentMethods = payment,
                    describeOrder = describeOrder,
                    orderStatus = "Chờ xác nhận",
                    adminId = ""
                )
                loading(true)
                cartViewModel.addOrder(order)
                cartViewModel.isCheck.observe(viewLifecycleOwner){check ->
                    if(check){
                        loading(false)
                        cartViewModel.getCartId()
                        cartViewModel.getCartId.observe(viewLifecycleOwner){
                            for(cartId in it){
                                cartViewModel.deleteCart(cartId)
                            }
                        }
                        val dialog = OrderSuccessDialog(object: OnClickListener {
                            override fun onClick() {
                                callback.showFragment(OrderFragment::class.java, HomeUserFragment::class.java, 0, 0, data,false)
                            }
                        })
                        dialog.show(requireActivity().supportFragmentManager, "order_success_dialog")
                    }
                }
            }
        }

        binding.btnBack.setOnClickListener {
            callback.backToPrevious()
        }
    }

    private fun loading(isLoading: Boolean){
        if(isLoading){
            binding.btnConfirmOrder.visibility = View.INVISIBLE
            binding.proBarBtnOrder.visibility = View.VISIBLE
        }else{
            binding.btnConfirmOrder.visibility = View.VISIBLE
            binding.proBarBtnOrder.visibility = View.INVISIBLE
        }
    }

}