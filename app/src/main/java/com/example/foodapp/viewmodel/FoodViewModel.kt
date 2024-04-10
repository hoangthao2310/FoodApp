package com.example.foodapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.foodapp.model.Category
import com.example.foodapp.model.Food
import com.example.foodapp.repository.FoodRepository

class FoodViewModel(application: Application)  : AndroidViewModel(application){
    private val foodRepository: FoodRepository
    private var foodLiveData: MutableLiveData<ArrayList<Food>>
    private var foodDetail: MutableLiveData<Food>
    private var categoryLiveData: MutableLiveData<ArrayList<Category>>

    val getFood: MutableLiveData<ArrayList<Food>>
        get() = foodLiveData

    val getFoodDetail: MutableLiveData<Food>
        get() = foodDetail
    val getCategory: MutableLiveData<ArrayList<Category>>
        get() = categoryLiveData
    init {
        foodRepository = FoodRepository(application)
        foodLiveData = foodRepository.getFoodFirebase
        foodDetail = foodRepository.getFoodDetail
        categoryLiveData = foodRepository.getCategoryFirebase
    }

    fun bestFood(){
        foodRepository.bestFood()
    }
    fun category(){
        foodRepository.category()
    }

    fun food(categoryId: String?){
        foodRepository.food(categoryId)
    }

    fun foodDetail(foodId: String?){
        foodRepository.foodDetail(foodId)
    }
}