package com.example.foodapp.view.cart

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.OnItemClickListener
import com.example.foodapp.adapter.InfoUserAdapter
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentLocationOrderBinding
import com.example.foodapp.model.CartAdmin
import com.example.foodapp.model.Location
import com.example.foodapp.viewmodel.AccountViewModel
import com.example.foodapp.viewmodel.CartViewModel

class LocationOrderFragment : BaseFragment<FragmentLocationOrderBinding>() {
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var infoUserAdapter: InfoUserAdapter
    private lateinit var cartAdmin: CartAdmin
    override fun getLayout(container: ViewGroup?): FragmentLocationOrderBinding =
        FragmentLocationOrderBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        cartAdmin = data as CartAdmin
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]

        accountViewModel.getLocation()
        accountViewModel.getLocation.observe(viewLifecycleOwner){ listLocation ->
            val onItemClickListener = object : OnItemClickListener{
                override fun onItemClick(data: Any?) {
                    val location = data as Location
                    accountViewModel.updateInfoUserOrder(location)
                    accountViewModel.getLocationStatus.observe(viewLifecycleOwner){
                        if(it){
                            parentFragmentManager.popBackStack()
                        }
                    }
                }

                override fun onItemAddClick(data: Any?) {
                }

                override fun onItemEditClick(data: Any?) {
                }

                override fun onItemDeleteClick(data: Any?) {
                }
            }
            infoUserAdapter = InfoUserAdapter(listLocation, onItemClickListener)
            binding.rcvAddress.adapter = infoUserAdapter
            binding.proBarAddress.visibility = View.INVISIBLE
        }

        binding.layoutAddAddress.setOnClickListener {
            callback.showFragment(LocationOrderFragment::class.java, AddInfoUserOrderFragment::class.java, 0 ,0, null, true)
        }

        binding.btnBack.setOnClickListener {
            callback.backToPrevious()
        }
    }

}