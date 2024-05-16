package com.example.foodapp.view.purchaseOrder

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.OnItemClickListener
import com.example.foodapp.adapter.PurchaseOrderAdapter
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentPurchaseOrderAdminBinding
import com.example.foodapp.model.Order
import com.example.foodapp.view.home.HomeAdminFragment
import com.example.foodapp.view.profileAdmin.ProfileAdminFragment
import com.example.foodapp.viewmodel.CartViewModel
import com.google.firebase.auth.FirebaseUser

class PurchaseOrderAdminFragment : BaseFragment<FragmentPurchaseOrderAdminBinding>() {
    private lateinit var cartViewModel: CartViewModel
    private lateinit var purchaseOrderAdapter: PurchaseOrderAdapter
    private lateinit var firebaseUser: FirebaseUser
    override fun getLayout(container: ViewGroup?): FragmentPurchaseOrderAdminBinding =
        FragmentPurchaseOrderAdminBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        firebaseUser = data as FirebaseUser
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]

        cartViewModel.getOrderAdmin(firebaseUser.uid)
        cartViewModel.getOrderFirebase.observe(viewLifecycleOwner){listOrder ->
            if(listOrder != null){
                val onItemClickListener = object: OnItemClickListener {
                    override fun onItemClick(data: Any?) {
                        val order = data as Order
                        callback.showFragment(PurchaseOrderAdminFragment::class.java, OrderDetailAdminFragment::class.java, 0,0, order, true)
                    }

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
            callback.showFragment(PurchaseOrderAdminFragment::class.java, HomeAdminFragment::class.java, 0, 0, data, true)
        }

        binding.btnProfile.setOnClickListener {
            callback.showFragment(PurchaseOrderAdminFragment::class.java, ProfileAdminFragment::class.java, 0, 0, data, true)
        }
    }

}