package com.example.foodapp.view

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.OnItemClickListener
import com.example.foodapp.adapter.CategoryAdapter
import com.example.foodapp.adapter.FoodAdapter
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentMainBinding
import com.example.foodapp.model.Category
import com.example.foodapp.model.Food
import com.example.foodapp.viewmodel.FoodViewModel
import com.example.foodapp.viewmodel.LogViewModel
import com.google.firebase.auth.FirebaseUser

class MainFragment : BaseFragment<FragmentMainBinding>() {
    private lateinit var foodViewModel: FoodViewModel
    private lateinit var logViewModel: LogViewModel
    private lateinit var foodAdapter: FoodAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var firebaseUser: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        foodViewModel = ViewModelProvider(this)[FoodViewModel::class.java]
        foodViewModel.bestFood()
        foodViewModel.category()
    }
    override fun getLayout(container: ViewGroup?): FragmentMainBinding =
        FragmentMainBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        logViewModel = ViewModelProvider(this)[LogViewModel::class.java]
        firebaseUser = data as FirebaseUser
        logViewModel.getUserDetail(firebaseUser.uid)
        logViewModel.getUser.observe(viewLifecycleOwner){
            binding.tvUserName.text = it.userName
        }

        foodViewModel.getFood.observe(viewLifecycleOwner){ listBestFood ->
            if(listBestFood != null){
                val onItemClickListener = object : OnItemClickListener{
                    override fun onItemClick(data: Any?) {
                        val food = data as Food
                        callback.showFragment(MainFragment::class.java, FoodDetailFragment::class.java, 0, 0, food, true)
                    }
                }
                foodAdapter = FoodAdapter(listBestFood, onItemClickListener)
                binding.rcvBestFood.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.rcvBestFood.adapter = foodAdapter
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
                }
                categoryAdapter = CategoryAdapter(listCategory, onItemClickListener)
                binding.rcvCategory.adapter = categoryAdapter
                binding.proBarCategory.visibility = View.INVISIBLE
            }

        }
    }
}