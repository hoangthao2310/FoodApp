package com.example.foodapp.view.purchaseOrder

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.OnItemClickListener
import com.example.foodapp.adapter.PurchaseOrderAdapter
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentPurchaseOrderBinding
import com.example.foodapp.view.home.HomeUserFragment
import com.example.foodapp.view.profileUser.ProfileFragment
import com.example.foodapp.viewmodel.CartViewModel

class PurchaseOrderFragment : BaseFragment<FragmentPurchaseOrderBinding>() {
    private lateinit var cartViewModel: CartViewModel
    private lateinit var purchaseOrderAdapter: PurchaseOrderAdapter
    override fun getLayout(container: ViewGroup?): FragmentPurchaseOrderBinding =
        FragmentPurchaseOrderBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]

        cartViewModel.getOrder()
        cartViewModel.getOrderFirebase.observe(viewLifecycleOwner){listOrder ->
            if(listOrder != null){
                val onItemClickListener = object: OnItemClickListener{
                    override fun onItemClick(data: Any?) {}

                    override fun onItemAddClick(data: Any?) {}

                    override fun onItemEditClick(data: Any?) {}

                    override fun onItemDeleteClick(data: Any?) {}
                }

                purchaseOrderAdapter = PurchaseOrderAdapter(listOrder, onItemClickListener)
                binding.rcvPurchaseOrder.adapter = purchaseOrderAdapter
                binding.progressBar.visibility = View.INVISIBLE
            }
        }

        binding.btnHome.setOnClickListener {
            callback.showFragment(PurchaseOrderFragment::class.java, HomeUserFragment::class.java, 0, 0, data, true)
        }

        binding.btnProfile.setOnClickListener {
            callback.showFragment(PurchaseOrderFragment::class.java, ProfileFragment::class.java, 0, 0, data, true)
        }

        binding.btnBack.setOnClickListener {
            callback.backToPrevious()
        }
    }

}