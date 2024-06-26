package com.example.foodapp.view.profileUser

import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentProfileBinding
import com.example.foodapp.view.home.HomeUserFragment
import com.example.foodapp.view.log.LoginFragment
import com.example.foodapp.view.profileUser.location.SavedLocationFragment
import com.example.foodapp.view.purchaseOrder.PurchaseOrderFragment
import com.example.foodapp.viewmodel.AccountViewModel
import com.google.firebase.auth.FirebaseUser

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var firebaseUser: FirebaseUser
    override fun getLayout(container: ViewGroup?): FragmentProfileBinding =
        FragmentProfileBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        firebaseUser = data as FirebaseUser
        accountViewModel.getUserDetail()
        accountViewModel.getUser.observe(viewLifecycleOwner){user->
            binding.tvUserName.text = user?.userName
            binding.tvEmail.text = user?.email
            Glide.with(requireActivity()).load(user?.imageUser).into(binding.imgUser)
        }

        binding.btnEdit.setOnClickListener {
            callback.showFragment(ProfileFragment::class.java, EditProfileFragment::class.java, 0, 0, data, true)
        }

        binding.btnFavouriteFood.setOnClickListener {
            callback.showFragment(ProfileFragment::class.java, FavouriteFoodFragment::class.java, 0, 0, data, true)
        }

        binding.btnLocation.setOnClickListener {
            callback.showFragment(ProfileFragment::class.java, SavedLocationFragment::class.java, 0,0, data, true)
        }

        binding.btnHome.setOnClickListener {
            callback.showFragment(ProfileFragment::class.java, HomeUserFragment::class.java, 0, 0, data, true)
        }
        binding.btnPurchaseOrder.setOnClickListener {
            callback.showFragment(ProfileFragment::class.java, PurchaseOrderFragment::class.java, 0, 0, data, true)
        }

        binding.btnLogout.setOnClickListener {
            accountViewModel.logout()
            callback.showFragment(ProfileFragment::class.java, LoginFragment::class.java, 0, 0, null, false)
        }
    }

}