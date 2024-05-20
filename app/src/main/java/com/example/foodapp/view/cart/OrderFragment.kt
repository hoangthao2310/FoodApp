package com.example.foodapp.view.cart

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.R
import com.example.foodapp.adapter.OrderAdapter
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentOrderBinding
import com.example.foodapp.model.CartAdmin
import com.example.foodapp.model.Order
import com.example.foodapp.view.purchaseOrder.PurchaseOrderFragment
import com.example.foodapp.viewmodel.AccountViewModel
import com.example.foodapp.viewmodel.CartViewModel


class OrderFragment : BaseFragment<FragmentOrderBinding>() {
    private lateinit var cartViewModel: CartViewModel
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var cartAdmin: CartAdmin

    private var delivery: String? = null
    private var payment: String? = null
    private var total: Double? = null
    private var totalPrice: Double? = 0.0
    private var describeOrder: String? = ""
    override fun getLayout(container: ViewGroup?): FragmentOrderBinding =
        FragmentOrderBinding.inflate(layoutInflater, container, false)

    @SuppressLint("SetTextI18n")
    override fun initViews() {
        cartAdmin = data as CartAdmin

        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        accountViewModel.getUserDetail()
        accountViewModel.getUser.observe(viewLifecycleOwner){user ->
            binding.tvUserName.text = user?.contactPersonName
            binding.tvPhoneNumber.text = user?.contactPhoneNumber
            binding.tvAddress.text = user?.address
        }

        binding.btnSelectAddressOrder.setOnClickListener {
            callback.showFragment(OrderFragment::class.java, LocationOrderFragment::class.java, 0, 0, cartAdmin,true)
        }

        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        cartViewModel.getCartDetail(cartAdmin.cartAdminId.toString())
        cartViewModel.getCartFirebase.observe(viewLifecycleOwner){
            orderAdapter = OrderAdapter(it)
            binding.rcvOrder.adapter = orderAdapter
            binding.proBarOrder.visibility = View.INVISIBLE

            for(cart in it){
                totalPrice = totalPrice!! + cart.intoMoney!!
            }
            binding.tvTotalFood.text = totalPrice.toString() + "đ"
            total = totalPrice!! + 10000.0
            binding.tvTotalPrice.text = total.toString() + "đ"
            binding.tvTotalOrder.text = total.toString() + "đ"
        }

        binding.radFastDelivery.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked){
                binding.tvTransportFee.text = "10000.0đ"
                total = totalPrice!! + 10000.0 - 10000.0
                binding.tvTotalPrice.text = total.toString() + "đ"
                binding.tvTotalOrder.text = total.toString() + "đ"
            }
        }

        binding.radExpressDelivery.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked){
                binding.tvTransportFee.text = "15000.0đ"
                total = totalPrice!! + 15000.0 - 10000.0
                binding.tvTotalPrice.text = total.toString() + "đ"
                binding.tvTotalOrder.text = total.toString() + "đ"
            }
        }

        binding.btnConfirmOrder.setOnClickListener {
            if(binding.tvUserName.text.isEmpty() || binding.tvPhoneNumber.text.isEmpty() || binding.tvAddress.text.isEmpty()){
                binding.tvUserName.error = "Vui lòng chọn thông tin nhận hàng!"
            }

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
                accountViewModel.getUserDetail()
                accountViewModel.getUser.observe(viewLifecycleOwner){user ->
                    accountViewModel.getFirebaseUser(user?.email.toString(), user?.password.toString())
                    accountViewModel.getUserData.observe(viewLifecycleOwner){firebaseUser ->
                        cartViewModel.getCartDetail(cartAdmin.adminId.toString())
                        cartViewModel.getCartFirebase.observe(viewLifecycleOwner){
                            for(cart in it){
                                describeOrder += cart.foodName + ": Giá: " + cart.price + ", Số lượng: " + cart.quantity + " "
                            }

                            val order = Order(
                                userName = binding.tvUserName.text.toString(),
                                phoneNumber = binding.tvPhoneNumber.text.toString(),
                                totalPrice = total,
                                address = binding.tvAddress.text.toString(),
                                deliveryMethods = delivery,
                                paymentMethods = payment,
                                describeOrder = describeOrder,
                                orderStatus = "Chờ xác nhận",
                                userId = firebaseUser.uid,
                                adminId = cartAdmin.adminId
                            )
                            loading()
                            cartViewModel.addOrder(order)

                            for(cart in it){
                                cartViewModel.deleteCartDetail(cart.foodId.toString())
                            }
                        }
                        cartViewModel.deleteCartAdmin(cartAdmin.cartAdminId.toString())
                        notify("Đặt hàng thành công")
                        callback.showFragment(OrderFragment::class.java, PurchaseOrderFragment::class.java, 0, 0, firebaseUser,false)
                        }
                    }
                }

        }

        binding.btnBack.setOnClickListener {
            callback.backToPrevious()
        }
    }

    private fun loading(){
        binding.btnConfirmOrder.visibility = View.INVISIBLE
        binding.proBarBtnOrder.visibility = View.VISIBLE
    }

}