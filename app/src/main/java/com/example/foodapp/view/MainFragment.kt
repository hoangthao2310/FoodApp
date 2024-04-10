package com.example.foodapp.view

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.OnItemClickListener
import com.example.foodapp.adapter.BestFoodAdapter
import com.example.foodapp.adapter.CategoryAdapter
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentMainBinding
import com.example.foodapp.model.Category
import com.example.foodapp.model.Food
import com.example.foodapp.view.cart.CartFragment
import com.example.foodapp.view.food.FoodDetailFragment
import com.example.foodapp.view.food.FoodFragment
import com.example.foodapp.view.profile.ProfileFragment
import com.example.foodapp.viewmodel.CartViewModel
import com.example.foodapp.viewmodel.FoodViewModel
import com.example.foodapp.viewmodel.LogViewModel
import com.google.firebase.auth.FirebaseUser

class MainFragment : BaseFragment<FragmentMainBinding>() {
    private lateinit var foodViewModel: FoodViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var logViewModel: LogViewModel
    private lateinit var bestFoodAdapter: BestFoodAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var firebaseUser: FirebaseUser
    override fun getLayout(container: ViewGroup?): FragmentMainBinding =
        FragmentMainBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        logViewModel = ViewModelProvider(this)[LogViewModel::class.java]
        firebaseUser = data as FirebaseUser
        logViewModel.getUserDetail(firebaseUser.uid)
        logViewModel.getUser.observe(viewLifecycleOwner){it ->
            binding.tvUserName.text = it.userName
        }

        binding.btnProfile.setOnClickListener {
            callback.showFragment(MainFragment::class.java, ProfileFragment::class.java, 0, 0, data)
        }

        foodViewModel = ViewModelProvider(this)[FoodViewModel::class.java]
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        foodViewModel.bestFood()
        foodViewModel.category()

        foodViewModel.getFood.observe(viewLifecycleOwner){ listBestFood ->
            if(listBestFood != null){
                val onItemClickListener = object : OnItemClickListener{
                    override fun onItemClick(data: Any?) {
                        val food = data as Food
                        callback.showFragment(MainFragment::class.java, FoodDetailFragment::class.java, 0, 0, food, true)
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
                bestFoodAdapter = BestFoodAdapter(listBestFood, onItemClickListener)
                binding.rcvBestFood.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.rcvBestFood.adapter = bestFoodAdapter
                binding.proBarBestFood.visibility = View.INVISIBLE
            }

        }

        foodViewModel.getCategory.observe(viewLifecycleOwner){ listCategory ->
            if(listCategory != null){
                val onItemClickListener = object : OnItemClickListener{
                    override fun onItemClick(data: Any?) {
                        val category = data as Category
                        callback.showFragment(MainFragment::class.java, FoodFragment::class.java, 0, 0, category, true)
                    }

                    override fun onItemAddCartClick(data: Any?) {}
                }
                categoryAdapter = CategoryAdapter(listCategory, onItemClickListener)
                binding.rcvCategory.adapter = categoryAdapter
                binding.proBarCategory.visibility = View.INVISIBLE
            }
        }
        binding.btnCart.setOnClickListener {
            callback.showFragment(MainFragment::class.java, CartFragment::class.java, 0, 0, null, true)
        }
    }
}