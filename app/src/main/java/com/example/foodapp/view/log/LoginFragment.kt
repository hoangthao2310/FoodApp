package com.example.foodapp.view.log

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.R
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentLoginBinding
import com.example.foodapp.until.PreferenceManager
import com.example.foodapp.view.home.HomeAdminFragment
import com.example.foodapp.view.home.HomeUserFragment
import com.example.foodapp.viewmodel.AccountViewModel


class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private lateinit var accountViewModel: AccountViewModel
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
    }

    override fun getLayout(container: ViewGroup?): FragmentLoginBinding =
        FragmentLoginBinding.inflate(layoutInflater, container, false)

    override fun initViews() {

        preferenceManager = PreferenceManager(requireContext())
        val isRemember = preferenceManager.getBoolean("isRemember")
        Log.d("LogPreferences", "isRemember: $isRemember")

        if(isRemember){
            val emailAdress = preferenceManager.getString("email")
            val pass = preferenceManager.getString("password")
            Log.d("LogPreferences", "Stored email: $emailAdress, Stored password: $pass")
            if(!emailAdress.isNullOrEmpty() && !pass.isNullOrEmpty()) {
                accountViewModel.login(emailAdress, pass, true)
                accountViewModel.getUserData.observe(viewLifecycleOwner){userData ->
                    accountViewModel.getLogStatus.observe(this){
                        if(it){
                            accountViewModel.getUserDetail()
                            accountViewModel.getUser.observe(viewLifecycleOwner){ user ->
                                if(user?.checkAdmin == true){
                                    loading(false)
                                    callback.showFragment(LoginFragment::class.java, HomeAdminFragment::class.java, 0,0, userData, true)
                                }else{
                                    loading(false)
                                    callback.showFragment(LoginFragment::class.java, HomeUserFragment::class.java, 0,0, userData, true)
                                }

                            }
                        }
                    }
                }
            } else {
                Log.e("LogPreferences", "Stored email or password is null or empty.")
            }
        }
        
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmailAdress.text.toString()
            val password = binding.edtPassword.text.toString()

            if(email.isEmpty()){
                binding.edtEmailAdress.error = getString(R.string.empty_email)
            }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.edtEmailAdress.error = getString(R.string.valid_email)
            }else if(password.isEmpty()){
                binding.edtPassword.error = getString(R.string.empty_pass)
            }else if(password.length < 6){
                binding.edtPassword.error = getString(R.string.length_pass)
            }else{
                loading(true)
                accountViewModel.login(email, password, true)
                accountViewModel.getUserData.observe(viewLifecycleOwner){userData ->
                    accountViewModel.getLogStatus.observe(this){
                        if(it){
                            accountViewModel.getUserDetail()
                            accountViewModel.getUser.observe(viewLifecycleOwner){ user ->
                                if(user?.checkAdmin == true){
                                    loading(false)
                                    callback.showFragment(LoginFragment::class.java, HomeAdminFragment::class.java, 0,0, userData, true)
                                }else{
                                    loading(false)
                                    callback.showFragment(LoginFragment::class.java, HomeUserFragment::class.java, 0,0, userData, true)
                                }

                            }
                        }else{
                            loading(false)
                        }
                    }
                }
            }
        }

        binding.tvSignup.setOnClickListener {
            callback.showFragment(this::class.java, SignupFragment::class.java, 0, 0, null, true)
        }

        binding.tvForgetPass.setOnClickListener {
            callback.showFragment(this::class.java, ForgetPassFragment::class.java, 0, 0, null, true)
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