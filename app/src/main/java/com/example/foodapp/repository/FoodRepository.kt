package com.example.foodapp.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.foodapp.model.Category
import com.example.foodapp.model.Food
import com.google.firebase.firestore.FirebaseFirestore

class FoodRepository(_application: Application) {
    private var listFoodLiveData: MutableLiveData<ArrayList<Food>>
    private var foodLivedata: MutableLiveData<Food>
    private var categoryLiveData: MutableLiveData<ArrayList<Category>>
    private var application: Application

    private val firebaseFirestore: FirebaseFirestore

    val getFoodFirebase: MutableLiveData<ArrayList<Food>>
        get() = listFoodLiveData

    val getFoodDetail: MutableLiveData<Food>
        get() = foodLivedata
    val getCategoryFirebase: MutableLiveData<ArrayList<Category>>
        get() = categoryLiveData
    init {
        application = _application
        listFoodLiveData = MutableLiveData<ArrayList<Food>>()
        foodLivedata = MutableLiveData<Food>()
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
                        val foodId = document.id
                        val foodName = document.getString("foodName")
                        val price = document.getDouble("price")
                        val time = document.getString("time")
                        val rating = document.getDouble("rating")
                        val image = document.getString("image")
                        val describe = document.getString("describe")
                        val bestFood = document.getBoolean("bestFood")
                        val adminId = document.getString("adminId")
                        val categoryId = document.getString("categoryId")
                        val food = Food(foodId, foodName, price, rating, time, image, describe, bestFood, adminId, categoryId)
                        listFood.add(food)
                    }
                    listFoodLiveData.postValue(listFood)
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
                        val categoryId = document.id
                        val categoryName = document.getString("categoryName")
                        val image = document.getString("image")
                        val category = Category(categoryId, categoryName, image)
                        listCategory.add(category)
                    }
                    categoryLiveData.postValue(listCategory)
                }
            }
            .addOnFailureListener {
                Log.d("getCategory", "Error getting category: $it")
            }
    }

    fun food(cateId: String?){
        firebaseFirestore.collection("food").whereEqualTo("categoryId", cateId)
            .get()
            .addOnCompleteListener { task ->
                if(task.isSuccessful && task.result != null){
                    val listFood = ArrayList<Food>()
                    for(document in task.result){
                        val foodId = document.id
                        val foodName = document.getString("foodName")
                        val price = document.getDouble("price")
                        val time = document.getString("time")
                        val rating = document.getDouble("rating")
                        val image = document.getString("image")
                        val describe = document.getString("describe")
                        val bestFood = document.getBoolean("bestFood")
                        val adminId = document.getString("adminId")
                        val categoryId = document.getString("categoryId")
                        val food = Food(foodId, foodName, price, rating, time, image, describe, bestFood, adminId, categoryId)
                        listFood.add(food)
                    }
                    listFoodLiveData.postValue(listFood)
                }
            }
            .addOnFailureListener {
                Log.d("getFood", "Error getting food: $it")
            }
    }

    fun foodDetail(foodId: String?){
        if (foodId != null) {
            firebaseFirestore.collection("food").document(foodId)
                .get()
                .addOnSuccessListener {
                    if(it.exists()){
                        val food = it.toObject(Food::class.java)
                        foodLivedata.postValue(food!!)
                    }
                }
                .addOnFailureListener {
                    Log.d("getFoodDetail", "Error getting food detail: $it")
                }
        }
    }
}