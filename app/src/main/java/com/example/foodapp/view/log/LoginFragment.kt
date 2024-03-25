package com.example.foodapp.view.log

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.R
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentLoginBinding
import com.example.foodapp.until.PreferenceManager
import com.example.foodapp.view.MainFragment
import com.example.foodapp.viewmodel.LogViewModel


class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private lateinit var logViewModel: LogViewModel
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logViewModel = ViewModelProvider(this)[LogViewModel::class.java]
    }

    override fun getLayout(container: ViewGroup?): FragmentLoginBinding =
        FragmentLoginBinding.inflate(layoutInflater, container, false)

    override fun initViews() {

//        preferenceManager = PreferenceManager(requireContext())
//        val emailAdress = preferenceManager.getString("email")
//        val pass = preferenceManager.getString("password")
//        logViewModel.login(emailAdress!!, pass!!)
//        logViewModel.getUserData.observe(viewLifecycleOwner){user ->
//            logViewModel.getLogStatus.observe(this){
//                if(it){
//                    loading(false)
//                    callback.showFragment(LoginFragment::class.java, MainFragment::class.java, 0,0, user)
//                }
//            }
//        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmailAdress.text.toString()
            val password = binding.edtPassword.text.toString()

            if(email.isEmpty()){
                binding.edtEmailAdress.error = getString(R.string.empty_email)
            }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.edtEmailAdress.error = getString(R.string.valid_email)
            }else if(password.isEmpty()){
                binding.edtPassword.error = getString(R.string.empty_pass)
            }else{
                loading(true)
                logViewModel.login(email, password)
                logViewModel.getUserData.observe(viewLifecycleOwner){userData ->
                    logViewModel.getLogStatus.observe(this){
                        if(it){
                            loading(false)
                            callback.showFragment(LoginFragment::class.java, MainFragment::class.java, 0,0, userData)
                        }
                    }
                }
            }
        }

        binding.tvSignup.setOnClickListener {
            callback.showFragment(this::class.java, SignupFragment::class.java, 0, 0)
        }

        binding.tvForgetPass.setOnClickListener {
            callback.showFragment(this::class.java, ForgetPassFragment::class.java, 0, 0)
        }
    }
    private fun loading(isLoading: Boolean){
        if(isLoading){
            binding.btnLogin.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.btnLogin.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

}