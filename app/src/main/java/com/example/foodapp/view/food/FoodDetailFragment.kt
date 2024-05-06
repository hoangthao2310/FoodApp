package com.example.foodapp.view.food

import android.annotation.SuppressLint
import android.util.Log
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentFoodDetailBinding
import com.example.foodapp.model.Food
import com.example.foodapp.viewmodel.AccountViewModel
import com.example.foodapp.viewmodel.CartViewModel
import com.example.foodapp.viewmodel.FoodViewModel

class FoodDetailFragment : BaseFragment<FragmentFoodDetailBinding>() {
    private lateinit var foodViewModel: FoodViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var food: Food
    override fun getLayout(container: ViewGroup?): FragmentFoodDetailBinding =
        FragmentFoodDetailBinding.inflate(layoutInflater, container, false)

    @SuppressLint("SetTextI18n")
    override fun initViews() {
        foodViewModel = ViewModelProvider(this)[FoodViewModel::class.java]
        food = data as Food
        foodViewModel.foodDetail(food.foodId.toString())
        Log.d("favourToDetail", food.toString())

        foodViewModel.getFoodDetail.observe(viewLifecycleOwner){food ->
            binding.tvFoodName.text = food.foodName
            binding.tvPrice.text = food.price.toString()
            binding.tvTime.text = food.time
            binding.tvDescribeFood.text = food.describe
            binding.ratingBar.rating = food.rating.toString().toFloat()
            binding.tvRating.text = "${food.rating.toString()} đánh giá"
            Glide.with(requireActivity()).load(food.image).into(binding.imgFood)
        }

        binding.btnPlus.setOnClickListener {
            val quantity = binding.tvQuantity.text.toString().toInt() + 1
            binding.tvQuantity.text = quantity.toString()
            val intoMoney = binding.tvQuantity.text.toString().toInt() * food.price!!
            binding.tvIntoMoney.text = intoMoney.toString()
        }

        binding.btnMinus.setOnClickListener {
            val quantity = binding.tvQuantity.text.toString().toInt() - 1
            if(quantity>=1){
                binding.tvQuantity.text = quantity.toString()
                val intoMoney = binding.tvQuantity.text.toString().toInt() * food.price!!
                binding.tvIntoMoney.text = intoMoney.toString()
            }
        }

        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        binding.btnAddToCart.setOnClickListener {
            cartViewModel.getCart(food.adminId.toString())
            cartViewModel.getCartFirebase.observe(viewLifecycleOwner){listItemCart->
                if(!listItemCart.contains(listItemCart.find { it.foodId == food.foodId })){
                    cartViewModel.addCart(food, binding.tvQuantity.text.toString().toInt(), binding.tvIntoMoney.text.toString().toDouble())
                    cartViewModel.isCheck.observe(viewLifecycleOwner){
                        if(it){
                            notify("Đã thêm vào giỏ hàng")
                        }
                    }
                }else{
                    notify("Đã có trong giỏ hàng")
                }
            }
        }

        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        binding.btnFavouriteFood.setOnClickListener {
            accountViewModel.addFavouriteFood(food)
        }

        binding.btnBack.setOnClickListener {
            callback.backToPrevious()
        }
    }
}