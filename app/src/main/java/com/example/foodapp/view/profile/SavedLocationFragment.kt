package com.example.foodapp.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentSavedLocationBinding

class SavedLocationFragment : BaseFragment<FragmentSavedLocationBinding>() {
    override fun getLayout(container: ViewGroup?): FragmentSavedLocationBinding =
        FragmentSavedLocationBinding.inflate(layoutInflater, container, false)

    override fun initViews() {

    }

}