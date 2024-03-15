package com.example.foodapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.foodapp.R
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentChangePassBinding

class ChangePassFragment : BaseFragment<FragmentChangePassBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getLayout(container: ViewGroup?): FragmentChangePassBinding =
        FragmentChangePassBinding.inflate(layoutInflater, container, false)

    override fun initViews() {

    }


}