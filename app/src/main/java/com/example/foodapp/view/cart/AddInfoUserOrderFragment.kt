package com.example.foodapp.view.cart

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentAddInfoUserOrderBinding
import com.example.foodapp.model.Location
import com.example.foodapp.viewmodel.AccountViewModel


class AddInfoUserOrderFragment : BaseFragment<FragmentAddInfoUserOrderBinding>() {
    private lateinit var accountViewModel: AccountViewModel
    override fun getLayout(container: ViewGroup?): FragmentAddInfoUserOrderBinding =
        FragmentAddInfoUserOrderBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]

        binding.btnSaveLocation.setOnClickListener {
            val newLocation = Location(
                addressName = binding.edtAddressName.text.toString(),
                address = binding.edtAddress.text.toString(),
                note = binding.edtNote.text.toString(),
                contactPersonName = binding.edtContactPersonName.text.toString(),
                contactPhoneNumber = binding.edtPhoneNumber.text.toString()
            )
            loading(true)
            accountViewModel.addLocation(newLocation)
            accountViewModel.getLocationStatus.observe(viewLifecycleOwner){
                if(it){
                    loading(false)
                    parentFragmentManager.popBackStack()
                    notify("Lưu thành công")
                }
            }
        }

        binding.btnBack.setOnClickListener {
            callback.backToPrevious()
        }

    }

    private fun loading(isLoading: Boolean){
        if(isLoading){
            binding.btnSaveLocation.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.btnSaveLocation.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

}