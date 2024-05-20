package com.example.foodapp.view.profileUser.location

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.OnItemClickListener
import com.example.foodapp.adapter.LocationAdapter
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentSavedLocationBinding
import com.example.foodapp.model.Location
import com.example.foodapp.view.profileUser.ProfileFragment
import com.example.foodapp.view.profileUser.location.dialog.DeleteDialog
import com.example.foodapp.view.profileUser.location.dialog.OnClickListener
import com.example.foodapp.viewmodel.AccountViewModel
import com.google.firebase.auth.FirebaseUser

class SavedLocationFragment : BaseFragment<FragmentSavedLocationBinding>() {
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var locationAdapter: LocationAdapter
    override fun getLayout(container: ViewGroup?): FragmentSavedLocationBinding =
        FragmentSavedLocationBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        accountViewModel.getUserDetail()
        accountViewModel.getUser.observe(viewLifecycleOwner){user ->
            accountViewModel.getFirebaseUser(user?.email.toString(), user?.password.toString())
            accountViewModel.getUserData.observe(viewLifecycleOwner){firebaseUser ->
                binding.btnBack.setOnClickListener {
                    callback.showFragment(SavedLocationFragment::class.java, ProfileFragment::class.java, 0, 0, firebaseUser, true)
                }
            }
            accountViewModel.getLocation(user?.userId.toString())
        }
        accountViewModel.getLocation.observe(viewLifecycleOwner){ listLocation ->
            if(listLocation != null){
                val onItemClickListener = object : OnItemClickListener{
                    override fun onItemClick(data: Any?) {
                        val location = data as Location
                        callback.showFragment(SavedLocationFragment::class.java, EditToSavedLocationFragment::class.java, 0, 0, location, true)
                    }

                    override fun onItemAddClick(data: Any?) {}

                    override fun onItemEditClick(data: Any?) {}


                    override fun onItemDeleteClick(data: Any?) {
                        if (data is Location) {
                            val location = data
                            val dialog = DeleteDialog(object: OnClickListener {
                                override fun onClick() {
                                    accountViewModel.deleteLocation(location.locationId.toString())
                                    val position = locationAdapter.listLocation.indexOf(location)
                                    locationAdapter.removeItem(position)
                                }
                            })
                            dialog.show(requireActivity().supportFragmentManager, "delete_dialog")
                        } else {
                            notify("Lỗi dữ liệu")
                        }
                    }
                }
                locationAdapter = LocationAdapter(listLocation, onItemClickListener)
                binding.rcvAddress.adapter = locationAdapter
                binding.proBarAddress.visibility = View.INVISIBLE
            }
        }
        binding.layoutAddAddress.setOnClickListener {
            callback.showFragment(SavedLocationFragment::class.java, MapsFragment::class.java, 0, 0, data, true)
        }
    }

}