package com.example.foodapp.view.log

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.R
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentSignupBinding
import com.example.foodapp.viewmodel.LogViewModel

class SignupFragment : BaseFragment<FragmentSignupBinding>() {

    private lateinit var logViewModel: LogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logViewModel = ViewModelProvider(this)[LogViewModel::class.java]
    }
    override fun getLayout(container: ViewGroup?): FragmentSignupBinding =
        FragmentSignupBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        binding.tvLogin.setOnClickListener {
            callback.showFragment(this::class.java, LoginFragment::class.java, 0, 0)
        }

        binding.btnSignup.setOnClickListener {
            val email = binding.edtEmailAdress.text.toString()
            val userName = binding.edtUserName.text.toString()
            val password = binding.edtPassword.text.toString()
            val confirmPass = binding.edtConfirmPass.text.toString()

            if(email.isEmpty()){
                binding.edtEmailAdress.error = getString(R.string.empty_email)
            }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.edtEmailAdress.error = getString(R.string.valid_email)
            }else if(userName.isEmpty()){
                binding.edtUserName.error = getString(R.string.empty_name)
            }else if(password.isEmpty()){
                binding.edtPassword.error = getString(R.string.empty_pass)
            }else if(password.length < 6){
                binding.edtPassword.error = getString(R.string.length_pass)
            }else if(password != confirmPass){
                binding.edtConfirmPass.error = getString(R.string.confirm_pass)
            }else{
                loading(true)
                logViewModel.register(email, password, userName)
                logViewModel.getLogStatus.observe(this){
                    if(it){
                        loading(false)
                        callback.showFragment(SignupFragment::class.java, LoginFragment::class.java, 0,0)
                    }
                }
            }
        }
    }

    private fun loading(isLoading: Boolean){
        if(isLoading){
            binding.btnSignup.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.btnSignup.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
        }
    }


}