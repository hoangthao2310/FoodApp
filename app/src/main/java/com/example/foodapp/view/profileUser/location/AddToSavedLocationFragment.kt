package com.example.foodapp.view.profileUser.location

import android.location.Address
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentAddToSavedLocationBinding
import com.example.foodapp.model.Location
import com.example.foodapp.viewmodel.AccountViewModel

class AddToSavedLocationFragment : BaseFragment<FragmentAddToSavedLocationBinding>() {
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var address: List<Address>
    override fun getLayout(container: ViewGroup?): FragmentAddToSavedLocationBinding =
        FragmentAddToSavedLocationBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        address = data as List<Address>

        binding.edtAddress.setText(address[0].getAddressLine(0))

        binding.btnSaveLocation.setOnClickListener {
            val newLocation = Location(
                addressName = binding.edtAddressName.text.toString(),
                address = binding.edtAddress.text.toString(),
                note = binding.edtNote.text.toString(),
                contactPersonName = binding.edtContactPersonName.text.toString(),
                contactPhoneNumber = binding.edtPhoneNumber.text.toString()
            )
            loading(true)
            if(binding.cbChooseDefault.isChecked){
                accountViewModel.updateInfoUserOrder(newLocation)
            }
            accountViewModel.addLocation(newLocation)
            accountViewModel.getLocationStatus.observe(viewLifecycleOwner){
                if(it){
                    loading(false)
                    callback.showFragment(AddToSavedLocationFragment::class.java, SavedLocationFragment::class.java, 0, 0, null, false)
                    notify("Lưu địa chỉ thành công")
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