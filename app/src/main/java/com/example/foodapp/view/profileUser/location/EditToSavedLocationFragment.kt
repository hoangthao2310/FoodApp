package com.example.foodapp.view.profileUser.location

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.R
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentEditToSavedLocationBinding
import com.example.foodapp.model.Location
import com.example.foodapp.view.profileUser.location.dialog.DeleteDialog
import com.example.foodapp.view.profileUser.location.dialog.OnClickListener
import com.example.foodapp.viewmodel.AccountViewModel

class EditToSavedLocationFragment : BaseFragment<FragmentEditToSavedLocationBinding>() {
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var location: Location
    override fun getLayout(container: ViewGroup?): FragmentEditToSavedLocationBinding =
        FragmentEditToSavedLocationBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        location = data as Location

        binding.edtAddressName.setText(location.addressName)
        binding.edtAddress.setText(location.address)
        binding.edtNote.setText(location.note)
        binding.edtContactPersonName.setText(location.contactPersonName)
        binding.edtPhoneNumber.setText(location.contactPhoneNumber)

        binding.btnDelete.setOnClickListener {
            val dialog = DeleteDialog(object: OnClickListener {
                override fun onClick() {
                    accountViewModel.deleteLocation(location.locationId.toString())
                    parentFragmentManager.popBackStack()
                }
            })
            dialog.show(requireActivity().supportFragmentManager, "delete_dialog")
        }

        binding.btnSaveEditLocation.setOnClickListener {
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
                    contactPhoneNumber = binding.edtPhoneNumber.text.toString()
                )
                loading(true)
                if(binding.cbChooseDefault.isChecked){
                    accountViewModel.updateInfoUserOrder(newLocation)
                }
                accountViewModel.updateLocation(location.locationId.toString(),newLocation)
                loading(false)
                parentFragmentManager.popBackStack()
                notify("Lưu thay đổi thành công")
            }
        }
        binding.btnBack.setOnClickListener {
            callback.backToPrevious()
        }
    }

    private fun loading(isLoading: Boolean){
        if(isLoading){
            binding.btnSaveEditLocation.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.btnSaveEditLocation.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

}