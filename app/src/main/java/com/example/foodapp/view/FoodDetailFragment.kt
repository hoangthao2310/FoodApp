package com.example.foodapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentFoodDetailBinding

class FoodDetailFragment : BaseFragment<FragmentFoodDetailBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun getLayout(container: ViewGroup?): FragmentFoodDetailBinding =
        FragmentFoodDetailBinding.inflate(layoutInflater, container, false)

    override fun initViews() {

    }





}