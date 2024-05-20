package com.example.foodapp.view.purchaseOrder

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentOrderDetailAdminBinding
import com.example.foodapp.model.Order
import com.example.foodapp.viewmodel.CartViewModel

class OrderDetailAdminFragment : BaseFragment<FragmentOrderDetailAdminBinding>() {
    private lateinit var cartViewModel: CartViewModel
    private lateinit var order: Order

    private val unconfirmed: String = "Chưa xác nhận"
    private val confirmed: String = "Đã xác nhận"
    private val orderCanceled: String = "Đã hủy đơn hàng"
    override fun getLayout(container: ViewGroup?): FragmentOrderDetailAdminBinding =
        FragmentOrderDetailAdminBinding.inflate(layoutInflater, container, false)

    @SuppressLint("SetTextI18n")
    override fun initViews() {
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        order = data as Order

        binding.tvUserName.text = order.userName
        binding.tvPhoneNumber.text = order.phoneNumber
        binding.tvAddress.text = order.address
        binding.tvDescribe.text = order.describeOrder
        binding.tvDeliveryMethod.text = "Hình thức giao hàng: " + order.deliveryMethods
        binding.tvPaymentMethod.text = "Hình thức thanh toán: " + order.paymentMethods
        when(order.orderStatus){
            confirmed -> {
                binding.tvOrderStatus.text = order.orderStatus
                binding.tvOrderStatus.setTextColor(Color.GREEN)
                binding.btnConfirmOrder.visibility = View.INVISIBLE
                binding.btnCancelOrder.visibility = View.INVISIBLE
            }
            orderCanceled -> {
                binding.tvOrderStatus.text = order.orderStatus
                binding.tvOrderStatus.setTextColor(Color.RED)
                binding.btnConfirmOrder.visibility = View.INVISIBLE
                binding.btnCancelOrder.visibility = View.INVISIBLE
            }
            unconfirmed -> {
                binding.tvOrderStatus.text = order.orderStatus
                binding.tvOrderStatus.setTextColor(Color.GRAY)
            }
        }
        binding.tvTotalPrice.text = order.totalPrice.toString() + "đ"

        when(binding.tvOrderStatus.text){
            unconfirmed -> {
                binding.btnConfirmOrder.setOnClickListener {
                    cartViewModel.updateOrder(order.orderId.toString(), confirmed)
                    binding.progressBar.visibility = View.VISIBLE
                    parentFragmentManager.popBackStack()
                }
                binding.btnCancelOrder.setOnClickListener {
                    cartViewModel.updateOrder(order.orderId.toString(), orderCanceled)
                    binding.progressBar2.visibility = View.VISIBLE
                    parentFragmentManager.popBackStack()
                }
            }
        }

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}