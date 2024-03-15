package com.example.foodapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodapp.R
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentLoginBinding


class LoginFragment : BaseFragment<FragmentLoginBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayout(container: ViewGroup?): FragmentLoginBinding =
        FragmentLoginBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        binding.tvSignup.setOnClickListener {
            callback.showFragment(this::class.java, SignupFragment::class.java, 0, 0)
        }

        binding.tvForgetPass.setOnClickListener {
            callback.showFragment(this::class.java, ForgetPassFragment::class.java, 0, 0)
        }
    }


}