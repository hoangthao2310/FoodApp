package com.example.foodapp.view.profileAdmin

import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentProfileAdminBinding
import com.example.foodapp.view.home.HomeAdminFragment
import com.example.foodapp.view.log.LoginFragment
import com.example.foodapp.viewmodel.AccountViewModel
import com.google.firebase.auth.FirebaseUser

class ProfileAdminFragment : BaseFragment<FragmentProfileAdminBinding>() {
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var firebaseUser: FirebaseUser
    override fun getLayout(container: ViewGroup?): FragmentProfileAdminBinding =
        FragmentProfileAdminBinding.inflate(layoutInflater, container, false)
    override fun initViews() {
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        firebaseUser = data as FirebaseUser
        accountViewModel.getUserDetail(firebaseUser.uid)
        accountViewModel.getUser.observe(viewLifecycleOwner){user ->
            binding.tvUserName.text = user?.userName
            binding.tvEmail.text = user?.email
            Glide.with(requireContext()).load(user?.imageUser).into(binding.imgUser)
        }

        binding.btnHome.setOnClickListener {
            callback.showFragment(ProfileAdminFragment::class.java, HomeAdminFragment::class.java, 0, 0, data, true)
        }
        binding.btnLogout.setOnClickListener {
            accountViewModel.logout()
            callback.showFragment(ProfileAdminFragment::class.java, LoginFragment::class.java, 0, 0, null, false)
        }
    }

}