package com.example.foodapp.view.home

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.OnItemClickListener
import com.example.foodapp.adapter.BestFoodAdapter
import com.example.foodapp.adapter.CategoryAdapter
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentHomeUserBinding
import com.example.foodapp.model.Category
import com.example.foodapp.model.Food
import com.example.foodapp.view.cart.CartAdminFragment
import com.example.foodapp.view.food.FoodDetailFragment
import com.example.foodapp.view.food.FoodFragment
import com.example.foodapp.view.profileUser.ProfileFragment
import com.example.foodapp.view.purchaseOrder.PurchaseOrderFragment
import com.example.foodapp.viewmodel.CartViewModel
import com.example.foodapp.viewmodel.FoodViewModel
import com.example.foodapp.viewmodel.AccountViewModel
import com.google.firebase.auth.FirebaseUser

class HomeUserFragment : BaseFragment<FragmentHomeUserBinding>() {
    private lateinit var foodViewModel: FoodViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var bestFoodAdapter: BestFoodAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var firebaseUser: FirebaseUser

    override fun getLayout(container: ViewGroup?): FragmentHomeUserBinding =
        FragmentHomeUserBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        firebaseUser = data as FirebaseUser
        accountViewModel.getUserDetail()
        accountViewModel.getUser.observe(viewLifecycleOwner){
            binding.tvUserName.text = it?.userName
        }

        binding.btnProfile.setOnClickListener {
            callback.showFragment(HomeUserFragment::class.java, ProfileFragment::class.java, 0, 0, data, true)
        }

        foodViewModel = ViewModelProvider(this)[FoodViewModel::class.java]
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        foodViewModel.bestFood()

        foodViewModel.getFood.observe(viewLifecycleOwner){ listBestFood ->
            if(listBestFood != null){
                val onItemClickListener = object : OnItemClickListener{
                    override fun onItemClick(data: Any?) {
                        val food = data as Food
                        callback.showFragment(HomeUserFragment::class.java, FoodDetailFragment::class.java, 0, 0, food, true)
                    }

                    override fun onItemAddClick(data: Any?) {
                    }

                    override fun onItemEditClick(data: Any?) {
                    }

                    override fun onItemDeleteClick(data: Any?) {
                    }
                }
                bestFoodAdapter = BestFoodAdapter(listBestFood, onItemClickListener)
                binding.rcvBestFood.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.rcvBestFood.adapter = bestFoodAdapter
                binding.proBarBestFood.visibility = View.INVISIBLE
            }

        }

        foodViewModel.category()
        foodViewModel.getCategory.observe(viewLifecycleOwner){ listCategory ->
            if(listCategory != null){
                val onItemClickListener = object : OnItemClickListener{
                    override fun onItemClick(data: Any?) {
                        val category = data as Category
                        callback.showFragment(HomeUserFragment::class.java, FoodFragment::class.java, 0, 0, category, true)
                    }

                    override fun onItemAddClick(data: Any?) {}
                    override fun onItemEditClick(data: Any?) {}
                    override fun onItemDeleteClick(data: Any?) {}
                }
                categoryAdapter = CategoryAdapter(listCategory, onItemClickListener)
                binding.rcvCategory.adapter = categoryAdapter
                binding.proBarCategory.visibility = View.INVISIBLE
            }
        }

        binding.btnCart.setOnClickListener {
            callback.showFragment(HomeUserFragment::class.java, CartAdminFragment::class.java, 0, 0, firebaseUser, true)
        }

        binding.btnPurchaseOrder.setOnClickListener {
            callback.showFragment(HomeUserFragment::class.java, PurchaseOrderFragment::class.java, 0,0, firebaseUser, true)
        }
    }
}