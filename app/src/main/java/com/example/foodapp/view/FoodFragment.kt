package com.example.foodapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentFoodBinding

class FoodFragment : BaseFragment<FragmentFoodBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun getLayout(container: ViewGroup?): FragmentFoodBinding =
        FragmentFoodBinding.inflate(layoutInflater, container, false)

    override fun initViews() {

    }
}