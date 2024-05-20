package com.example.foodapp.view.log

import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.R
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentChangePassBinding
import com.example.foodapp.model.User
import com.example.foodapp.viewmodel.AccountViewModel

class ChangePassFragment : BaseFragment<FragmentChangePassBinding>() {
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var user: User

    override fun getLayout(container: ViewGroup?): FragmentChangePassBinding =
        FragmentChangePassBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        user = data as User

        binding.btnChangePass.setOnClickListener {
            val password = binding.edtNewPassword.text.toString()
            val confirmPass = binding.edtConfirmNewPass.text.toString()

            if(password.isEmpty()){
                binding.edtNewPassword.error = getString(R.string.empty_pass)
            }else if(password.length < 6){
                binding.edtNewPassword.error = getString(R.string.length_pass)
            }else if (confirmPass.isEmpty()){
                binding.edtConfirmNewPass.error = getString(R.string.empty_confirm_pass)
            }else if(password != confirmPass){
                binding.edtConfirmNewPass.error = getString(R.string.confirm_pass)
            }else{
                accountViewModel.updatePassword(user.userId.toString(), password.toString())
                callback.showFragment(ChangePassFragment::class.java, LoginFragment::class.java, 0,0, null, true)
            }
        }
    }


}