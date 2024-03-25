package com.example.foodapp.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.foodapp.model.Category
import com.example.foodapp.model.Food
import com.google.firebase.firestore.FirebaseFirestore

class FoodRepository(_application: Application) {
    private var foodLiveData: MutableLiveData<ArrayList<Food>>
    private var categoryLiveData: MutableLiveData<ArrayList<Category>>
    private var application: Application

    private val firebaseFirestore: FirebaseFirestore

    val getFoodFirebase: MutableLiveData<ArrayList<Food>>
        get() = foodLiveData

    val getCategoryFirebase: MutableLiveData<ArrayList<Category>>
        get() = categoryLiveData
    init {
        application = _application
        foodLiveData = MutableLiveData<ArrayList<Food>>()
        categoryLiveData = MutableLiveData<ArrayList<Category>>()
        firebaseFirestore = FirebaseFirestore.getInstance()
    }

    fun bestFood(){
        firebaseFirestore.collection("food").whereEqualTo("bestFood", true)
            .get()
            .addOnCompleteListener { task ->
                if(task.isSuccessful && task.result != null){
                    val listFood = ArrayList<Food>()
                    for(document in task.result){
                        val foodName = document.getString("foodName")
                        val price = document.getDouble("price")
                        val time = document.getString("time")
                        val rating = document.getDouble("rating")
                        val image = document.getString("image")
                        val bestFood = document.getBoolean("bestFood")
                        val adminId = document.getString("adminId")
                        val categoryId = document.getString("categoryId")
                        val food = Food(foodName, price, rating, time, image, bestFood, adminId, categoryId)
                        listFood.add(food)
                        Log.d("food", food.toString())
                    }
                    foodLiveData.postValue(listFood)
                }
            }
            .addOnFailureListener {
                Log.d("getBestFood", "Error getting best food: $it")
            }
    }

    fun category(){
        firebaseFirestore.collection("category")
            .get()
            .addOnCompleteListener { task ->
                if(task.isSuccessful && task.result != null){
                    val listCategory = ArrayList<Category>()
                    for(document in task.result){
                        val categoryName = document.getString("categoryName")
                        val image = document.getString("image")
                        val category = Category(categoryName, image)
                        listCategory.add(category)
                        Log.d("category", category.toString())
                    }
                    categoryLiveData.postValue(listCategory)
                }
            }
            .addOnFailureListener {
                Log.d("getCategory", "Error getting category: $it")
            }
    }
}