package com.example.foodapp.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.foodapp.model.Category
import com.example.foodapp.model.Food
import com.example.foodapp.repository.FoodRepository

class FoodViewModel(application: Application)  : AndroidViewModel(application){
    private val foodRepository: FoodRepository
    private var foodLiveData: MutableLiveData<ArrayList<Food>>
    private var foodDetail: MutableLiveData<Food>
    private var category: MutableLiveData<ArrayList<String>>
    private var categoryLiveData: MutableLiveData<ArrayList<Category>>
    private var categoryDetail: MutableLiveData<String>
    private var check: MutableLiveData<Boolean>

    val getFood: MutableLiveData<ArrayList<Food>>
        get() = foodLiveData

    val getFoodDetail: MutableLiveData<Food>
        get() = foodDetail
    val getCategory: MutableLiveData<ArrayList<Category>>
        get() = categoryLiveData
    val getCategoryDetail: MutableLiveData<ArrayList<String>>
        get() = category
    val isCheck: MutableLiveData<Boolean>
        get() = check
    val getCateDetail: MutableLiveData<String>
        get() = categoryDetail
    init {
        foodRepository = FoodRepository(application)
        foodLiveData = foodRepository.getFoodFirebase
        foodDetail = foodRepository.getFoodDetail
        category = foodRepository.getCategory
        check = foodRepository.getCheck
        categoryDetail = foodRepository.getCateDetail
        categoryLiveData = foodRepository.getCategoryFirebase
    }

    fun bestFood(){
        foodRepository.bestFood()
    }
    fun category(){
        foodRepository.category()
    }
    fun categoryName(){
        foodRepository.categoryName()
    }

    fun food(categoryId: String?){
        foodRepository.food(categoryId)
    }

    fun foodDetail(foodId: String?){
        foodRepository.foodDetail(foodId)
    }

    fun foodAdmin(id: String){
        foodRepository.foodAdmin(id)
    }
    fun deleteFoodAdmin(foodId: String){
        foodRepository.deleteFoodAdmin(foodId)
    }
    fun addFood(food: Food, image: Uri){
        foodRepository.addFood(food, image)
    }
    fun categoryId(categoryName: String){
        foodRepository.getCategoryId(categoryName)
    }
    fun getCategoryName(categoryId: String){
        foodRepository.getCategoryName(categoryId)
    }
    fun foodDetailAdmin(foodId: String){
        foodRepository.foodDetailAdmin(foodId)
    }
    fun updateFoodAdmin(food: Food, foodId: String?){
        foodRepository.updateFoodAdmin(food, foodId)
    }
    fun updateImage(foodId: String, image: Uri){
        foodRepository.updateImage(foodId, image)
    }
}