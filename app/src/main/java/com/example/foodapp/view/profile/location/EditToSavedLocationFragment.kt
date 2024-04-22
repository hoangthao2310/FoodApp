package com.example.foodapp.view.profile.location

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentEditToSavedLocationBinding
import com.example.foodapp.model.Location
import com.example.foodapp.view.profile.location.dialog.DeleteDialog
import com.example.foodapp.view.profile.location.dialog.OnClickListener
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
                    callback.backToPrevious()
                }
            })
            dialog.show(requireActivity().supportFragmentManager, "delete_dialog")
        }

        binding.btnSaveEditLocation.setOnClickListener {
            val newLocation = Location(
                addressName = binding.edtAddressName.text.toString(),
                address = binding.edtAddress.text.toString(),
                note = binding.edtNote.text.toString(),
                contactPersonName = binding.edtContactPersonName.text.toString(),
                contactPhoneNumber = binding.edtPhoneNumber.text.toString()
            )
            loading(true)
            accountViewModel.updateLocation(location.locationId.toString(),newLocation)
            accountViewModel.getLocationStatus.observe(viewLifecycleOwner){
                if(it){
                    loading(false)
                    callback.showFragment(
                        EditToSavedLocationFragment::class.java,
                        SavedLocationFragment::class.java,
                        0,
                        0,
                    )
                    notify("Lưu thay đổi thành công")
                }
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