package com.example.foodapp.view.log

import android.util.Patterns
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.R
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentForgetPassBinding
import com.example.foodapp.viewmodel.AccountViewModel

class ForgetPassFragment : BaseFragment<FragmentForgetPassBinding>() {
    private lateinit var accountViewModel: AccountViewModel

    override fun getLayout(container: ViewGroup?): FragmentForgetPassBinding =
        FragmentForgetPassBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]

        binding.btnForgetPass.setOnClickListener {
            val email = binding.edtEmailAdress.text.toString()

            if(email.isEmpty()){
                binding.edtEmailAdress.error = getString(R.string.empty_email)
            }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.edtEmailAdress.error = getString(R.string.valid_email)
            }else{
                accountViewModel.getListUser()
                accountViewModel.getListUser.observe(viewLifecycleOwner){listUser ->
                    if(listUser != null){
                        for (user in listUser){
                            if(email == user.email.toString()){
                                callback.showFragment(this::class.java, ChangePassFragment::class.java, 0,0, user, true)
                            }
                        }
                        binding.edtEmailAdress.error = "Email không tồn tại!"
                    }
                }
            }

        }

        binding.tvLogin.setOnClickListener {
            callback.showFragment(this::class.java, LoginFragment::class.java, 0, 0)
        }

        binding.tvSignup.setOnClickListener {
            callback.showFragment(this::class.java, SignupFragment::class.java, 0, 0)
        }
    }
}