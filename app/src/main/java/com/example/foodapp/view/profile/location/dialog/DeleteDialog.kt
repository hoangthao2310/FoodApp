package com.example.foodapp.view.profile.location.dialog

import android.view.ViewGroup
import com.example.foodapp.base.BaseDialog
import com.example.foodapp.databinding.DialogDeleteBinding

class DeleteDialog(private var listener: OnClickListener): BaseDialog<DialogDeleteBinding>() {

    override fun getLayout(container: ViewGroup?): DialogDeleteBinding =
        DialogDeleteBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        onDeleteNote()
        onNoDelete()
    }

    private fun onNoDelete() {
        binding.btnNo.setOnClickListener {
            dialog?.cancel()
        }
    }

    private fun onDeleteNote() {
        binding.btnYes.setOnClickListener {
            listener.onClick()
            dismiss()
        }
    }
}