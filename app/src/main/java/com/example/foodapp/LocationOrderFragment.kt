package com.example.foodapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentLocationOrderBinding

class LocationOrderFragment : BaseFragment<FragmentLocationOrderBinding>() {
    override fun getLayout(container: ViewGroup?): FragmentLocationOrderBinding =
        FragmentLocationOrderBinding.inflate(layoutInflater, container, false)

    override fun initViews() {

    }

}