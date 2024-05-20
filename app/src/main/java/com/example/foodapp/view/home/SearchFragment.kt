package com.example.foodapp.view.home

import android.content.Context
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.OnItemClickListener
import com.example.foodapp.adapter.FoodAdapter
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentSearchBinding
import com.example.foodapp.model.Food
import com.example.foodapp.view.food.FoodDetailFragment
import com.example.foodapp.viewmodel.AccountViewModel
import com.example.foodapp.viewmodel.CartViewModel
import com.example.foodapp.viewmodel.FoodViewModel
import java.util.Locale

class SearchFragment : BaseFragment<FragmentSearchBinding>() {
    private lateinit var foodViewModel: FoodViewModel
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var foodAdapter: FoodAdapter
    private lateinit var searchList: ArrayList<Food>
    private var list: String? = ""

    override fun onResume() {
        super.onResume()
        binding.searchFood.requestFocus()
        showKeyboard()
    }
    override fun getLayout(container: ViewGroup?): FragmentSearchBinding =
        FragmentSearchBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        foodViewModel = ViewModelProvider(this)[FoodViewModel::class.java]
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]

        searchList = arrayListOf()
        binding.searchFood.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchFood.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

        foodViewModel.getAllFood()
        foodViewModel.getFood.observe(viewLifecycleOwner) { listFood ->
            if (listFood != null) {
                val onItemClickListener = object : OnItemClickListener {
                    override fun onItemClick(data: Any?) {
                        val food = data as Food
                        callback.showFragment(
                            SearchFragment::class.java,
                            FoodDetailFragment::class.java,
                            0,
                            0,
                            food,
                            true
                        )
                    }

                    override fun onItemAddClick(data: Any?) {
                        val food = data as Food
                        list += food.foodName.toString() + " "
                        accountViewModel.getUserDetail(food.adminId.toString())
                        accountViewModel.getUser.observe(viewLifecycleOwner){user ->
                            cartViewModel.addCartAdmin(food.adminId.toString(), user?.userName.toString(), list.toString())
                        }
                        cartViewModel.addCartDetail(food, 1, food.price!!)
                        cartViewModel.getCartDetail(food.adminId.toString())
                        notify("Đã thêm vào giỏ hàng")
                    }

                    override fun onItemEditClick(data: Any?) {
                    }

                    override fun onItemDeleteClick(data: Any?) {
                    }
                }
                searchList = listFood
                foodAdapter = FoodAdapter(listFood, onItemClickListener)
            }
        }

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun filterList(query: String?) {
        if(query != null){
            val filteredList = ArrayList<Food>()
            for(i in searchList){
                if(i.foodName?.lowercase(Locale.ROOT)?.contains(query) == true){
                    filteredList.add(i)
                }
            }

            if(filteredList.isNotEmpty()){
                foodAdapter.setFilteredList(filteredList)
                binding.rcvFood.adapter = foodAdapter
            }
        }
    }

    private fun showKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.searchFood.findFocus(), InputMethodManager.SHOW_IMPLICIT)
    }

}