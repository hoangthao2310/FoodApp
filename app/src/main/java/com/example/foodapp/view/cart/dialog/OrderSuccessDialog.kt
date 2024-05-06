package com.example.foodapp.view.cart.dialog

import android.view.ViewGroup
import com.example.foodapp.base.BaseDialog
import com.example.foodapp.databinding.DialogOrderSuccessBinding

class OrderSuccessDialog(private var listener: OnClickListener): BaseDialog<DialogOrderSuccessBinding>() {

    override fun getLayout(container: ViewGroup?): DialogOrderSuccessBinding =
        DialogOrderSuccessBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        onBackHome()
    }

    private fun onBackHome() {
        binding.btnBackHome.setOnClickListener {
            listener.onClick()
            dismiss()
        }
    }
}