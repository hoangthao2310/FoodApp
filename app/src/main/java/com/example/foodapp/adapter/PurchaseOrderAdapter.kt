package com.example.foodapp.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.OnItemClickListener
import com.example.foodapp.R
import com.example.foodapp.databinding.ItemPurchaseOrderBinding
import com.example.foodapp.model.Order

class PurchaseOrderAdapter(
    val listOrder: List<Order>,
    var itemClick: OnItemClickListener
): RecyclerView.Adapter<PurchaseOrderAdapter.PurchaseOrderViewHolder>() {

    inner class PurchaseOrderViewHolder(private val binding: ItemPurchaseOrderBinding): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(order: Order){
            binding.tvUserName.text = order.userName
            binding.tvPhoneNumber.text = order.phoneNumber
            binding.tvAddress.text = order.address
            binding.tvDescribe.text = order.describeOrder
            binding.tvTotalPrice.text = "${order.totalPrice}đ"

            when(order.orderStatus){
                "Chưa xác nhận" -> {
                    binding.tvStatusOrder.text = order.orderStatus
                    binding.tvStatusOrder.setTextColor(Color.GRAY)
                }
                "Đã xác nhận" -> {
                    binding.tvStatusOrder.text = order.orderStatus
                    binding.tvStatusOrder.setTextColor(Color.GREEN)
                }
                "Đã hủy đơn hàng" -> {
                    binding.tvStatusOrder.text = order.orderStatus
                    binding.tvStatusOrder.setTextColor(Color.RED)
                }
            }
        }

        init {
            binding.loItemPurchaseOrder.setOnClickListener {
                itemClick.onItemClick(listOrder[adapterPosition])
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PurchaseOrderAdapter.PurchaseOrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemPurchaseOrderBinding.inflate(inflater, parent, false)
        return PurchaseOrderViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: PurchaseOrderAdapter.PurchaseOrderViewHolder,
        position: Int
    ) {
        holder.bind(listOrder[position])
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }
}