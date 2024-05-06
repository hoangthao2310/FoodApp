package com.example.foodapp.view.profileUser

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.OnItemClickListener
import com.example.foodapp.adapter.FavouriteFoodAdapter
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentFavouriteFoodBinding
import com.example.foodapp.model.Food
import com.example.foodapp.view.food.FoodDetailFragment
import com.example.foodapp.view.profileUser.location.dialog.DeleteDialog
import com.example.foodapp.view.profileUser.location.dialog.OnClickListener
import com.example.foodapp.viewmodel.AccountViewModel

class FavouriteFoodFragment : BaseFragment<FragmentFavouriteFoodBinding>() {
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var favouriteFoodAdapter: FavouriteFoodAdapter
    override fun getLayout(container: ViewGroup?): FragmentFavouriteFoodBinding =
        FragmentFavouriteFoodBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        accountViewModel.getFavouriteFood()
        accountViewModel.getFavouriteFood.observe(viewLifecycleOwner){list ->
            if(list!=null){
                val onItemClickListener = object : OnItemClickListener {
                    override fun onItemClick(data: Any?) {
                        val food = data as Food
                        callback.showFragment(FavouriteFoodFragment::class.java, FoodDetailFragment::class.java, 0, 0, food, true)
                        Log.d("favour", food.toString())
                    }

                    override fun onItemAddClick(data: Any?) {}

                    override fun onItemEditClick(data: Any?) {}

                    override fun onItemDeleteClick(data: Any?) {
                        if (data is Food) {
                            val food = data
                            val dialog = DeleteDialog(object: OnClickListener {
                                override fun onClick() {
                                    accountViewModel.deleteFavouriteFood(food.favouriteId.toString())
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
                favouriteFoodAdapter = FavouriteFoodAdapter(list, onItemClickListener)
                binding.rcvFavouriteFood.adapter = favouriteFoodAdapter
                binding.proBarFavouriteFood.visibility = View.INVISIBLE
            }
        }
        binding.btnBack.setOnClickListener {
            callback.backToPrevious()
        }
    }

}