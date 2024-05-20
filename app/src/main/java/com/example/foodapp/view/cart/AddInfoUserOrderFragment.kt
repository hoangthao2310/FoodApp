package com.example.foodapp.view.cart

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.R
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
            accountViewModel.getUserDetail()
            accountViewModel.getUser.observe(viewLifecycleOwner) { user ->
                if(binding.edtAddressName.text.isEmpty()){
                    binding.edtAddressName.error = getString(R.string.isEmpty)
                }else if(binding.edtAddress.text.isEmpty()){
                    binding.edtAddress.error = getString(R.string.isEmpty)
                }else if(binding.edtNote.text.isEmpty()){
                    binding.edtNote.error = getString(R.string.isEmpty)
                }else if(binding.edtContactPersonName.text.isEmpty()){
                    binding.edtContactPersonName.error = getString(R.string.isEmpty)
                }else if(binding.edtPhoneNumber.text.isEmpty()){
                    binding.edtPhoneNumber.error = getString(R.string.isEmpty)
                }else{
                    val newLocation = Location(
                        addressName = binding.edtAddressName.text.toString(),
                        address = binding.edtAddress.text.toString(),
                        note = binding.edtNote.text.toString(),
                        contactPersonName = binding.edtContactPersonName.text.toString(),
                        contactPhoneNumber = binding.edtPhoneNumber.text.toString(),
                        userId = user?.userId.toString()
                    )
                    loading(true)
                    accountViewModel.addLocation(newLocation)
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