package com.example.foodapp.view.log

import android.os.Bundle
import android.view.ViewGroup
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentForgetPassBinding

class ForgetPassFragment : BaseFragment<FragmentForgetPassBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun getLayout(container: ViewGroup?): FragmentForgetPassBinding =
        FragmentForgetPassBinding.inflate(layoutInflater, container, false)

    override fun initViews() {

        binding.btnForgetPass.setOnClickListener {
            callback.showFragment(this::class.java, ChangePassFragment::class.java, 0,0)
        }

        binding.tvLogin.setOnClickListener {
            callback.showFragment(this::class.java, LoginFragment::class.java, 0, 0)
        }

        binding.tvSignup.setOnClickListener {
            callback.showFragment(this::class.java, SignupFragment::class.java, 0, 0)
        }
    }




}