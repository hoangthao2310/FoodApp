package com.example.foodapp.view.food

import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentFoodDetailBinding
import com.example.foodapp.model.Food
import com.example.foodapp.viewmodel.FoodViewModel

class FoodDetailFragment : BaseFragment<FragmentFoodDetailBinding>() {
    private lateinit var foodViewModel: FoodViewModel
    private lateinit var food: Food
    override fun getLayout(container: ViewGroup?): FragmentFoodDetailBinding =
        FragmentFoodDetailBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        foodViewModel = ViewModelProvider(this)[FoodViewModel::class.java]
        food = data as Food
        foodViewModel.foodDetail(food.foodId.toString())

        foodViewModel.getFoodDetail.observe(viewLifecycleOwner){food ->
            binding.tvFoodName.text = food.foodName
            binding.tvPrice.text = food.price.toString()
            binding.tvTime.text = food.time
            binding.tvDescribeFood.text = food.describe
            Glide.with(requireActivity()).load(food.image).into(binding.imgFood)
        }

        binding.btnBack.setOnClickListener {
            callback.backToPrevious()
        }
    }





}