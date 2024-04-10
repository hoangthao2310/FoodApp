package com.example.foodapp.view.food

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.OnItemClickListener
import com.example.foodapp.adapter.FoodAdapter
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentFoodBinding
import com.example.foodapp.model.Category
import com.example.foodapp.model.Food
import com.example.foodapp.viewmodel.CartViewModel
import com.example.foodapp.viewmodel.FoodViewModel

class FoodFragment : BaseFragment<FragmentFoodBinding>() {
    private lateinit var foodViewModel: FoodViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var foodAdapter: FoodAdapter
    private lateinit var category: Category

    override fun getLayout(container: ViewGroup?): FragmentFoodBinding =
        FragmentFoodBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        foodViewModel = ViewModelProvider(this)[FoodViewModel::class.java]
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        category = data as Category
        foodViewModel.food(category.categoryId)

        binding.tvCategoryName.text = category.categoryName

        foodViewModel.getFood.observe(viewLifecycleOwner){ listFood ->
            if(listFood != null){
                val onItemClickListener = object: OnItemClickListener{
                    override fun onItemClick(data: Any?) {
                        val food = data as Food
                        callback.showFragment(FoodFragment::class.java, FoodDetailFragment::class.java, 0, 0, food, true)
                    }

                    override fun onItemAddCartClick(data: Any?) {
                        val food = data as Food
                        cartViewModel.addCart(food, 1)
                        cartViewModel.isCheck.observe(viewLifecycleOwner){
                            if(it){
                                notify("Đã thêm vào giỏ hàng")
                            }
                        }
                    }

                }
                foodAdapter = FoodAdapter(listFood, onItemClickListener)
                binding.rcvFood.adapter = foodAdapter
                binding.progressBarFood.visibility = View.INVISIBLE
            }
        }
        binding.btnBack.setOnClickListener {
            callback.backToPrevious()
        }
    }
}