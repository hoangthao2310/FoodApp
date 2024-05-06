package com.example.foodapp.view.home

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.view.food.AddFoodFragment
import com.example.foodapp.view.food.EditFoodFragment
import com.example.foodapp.OnItemClickListener
import com.example.foodapp.adapter.FavouriteFoodAdapter
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentHomeAdminBinding
import com.example.foodapp.model.Food
import com.example.foodapp.view.profileAdmin.ProfileAdminFragment
import com.example.foodapp.view.profileUser.location.dialog.DeleteDialog
import com.example.foodapp.view.profileUser.location.dialog.OnClickListener
import com.example.foodapp.viewmodel.FoodViewModel
import com.google.firebase.auth.FirebaseUser


class HomeAdminFragment : BaseFragment<FragmentHomeAdminBinding>() {
    private lateinit var foodViewModel: FoodViewModel
    private lateinit var favouriteFoodAdapter: FavouriteFoodAdapter
    private lateinit var firebaseUser: FirebaseUser
    override fun getLayout(container: ViewGroup?): FragmentHomeAdminBinding =
        FragmentHomeAdminBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        foodViewModel = ViewModelProvider(this)[FoodViewModel::class.java]
        firebaseUser = data as FirebaseUser

        Log.d("initViews", firebaseUser.uid)

        foodViewModel.foodAdmin(firebaseUser.uid)
        foodViewModel.getFood.observe(viewLifecycleOwner){listFood ->
            if(listFood != null) {
                val onItemClickListener = object : OnItemClickListener {
                    override fun onItemClick(data: Any?) {
                        val food = data as Food
                        callback.showFragment(
                            HomeAdminFragment::class.java,
                            EditFoodFragment::class.java,
                            0,
                            0,
                            food,
                            true
                        )
                    }

                    override fun onItemAddClick(data: Any?) {}

                    override fun onItemEditClick(data: Any?) {}

                    override fun onItemDeleteClick(data: Any?) {
                        if (data is Food) {
                            val food = data
                            val dialog = DeleteDialog(object : OnClickListener {
                                override fun onClick() {
                                    foodViewModel.deleteFoodAdmin(food.foodId.toString())
                                    val position = favouriteFoodAdapter.listFood.indexOf(food)
                                    favouriteFoodAdapter.removeItem(position)
                                }
                            })
                            dialog.show(requireActivity().supportFragmentManager, "delete_dialog")
                        } else {
                            notify("Lỗi dữ liệu")
                        }
                    }
                }
                favouriteFoodAdapter = FavouriteFoodAdapter(listFood, onItemClickListener)
                binding.rcvFood.adapter = favouriteFoodAdapter
                binding.progressBar.visibility = View.INVISIBLE
            }
        }

        binding.btnAddNewFood.setOnClickListener {
            callback.showFragment(HomeAdminFragment::class.java, AddFoodFragment::class.java, 0, 0, data, true)
        }

        binding.btnProfile.setOnClickListener {
            callback.showFragment(HomeAdminFragment::class.java, ProfileAdminFragment::class.java, 0, 0, data, true)
        }
    }

}