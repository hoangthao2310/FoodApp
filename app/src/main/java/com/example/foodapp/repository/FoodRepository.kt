package com.example.foodapp.repository

import android.app.Application
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.foodapp.model.Category
import com.example.foodapp.model.Food
import com.example.foodapp.until.PreferenceManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class FoodRepository(_application: Application) {
    private var listFoodLiveData: MutableLiveData<ArrayList<Food>>
    private var foodLivedata: MutableLiveData<Food>
    private var category: MutableLiveData<ArrayList<String>>
    private var categoryDetail: MutableLiveData<String>
    private var categoryLiveData: MutableLiveData<ArrayList<Category>>
    private var check: MutableLiveData<Boolean>
    private var application: Application

    private val firebaseFirestore: FirebaseFirestore
    private val storageReference: StorageReference

    val getFoodFirebase: MutableLiveData<ArrayList<Food>>
        get() = listFoodLiveData
    val getFoodDetail: MutableLiveData<Food>
        get() = foodLivedata
    val getCategoryFirebase: MutableLiveData<ArrayList<Category>>
        get() = categoryLiveData
    val getCategory: MutableLiveData<ArrayList<String>>
        get() = category
    val getCheck: MutableLiveData<Boolean>
        get() = check
    val getCateDetail: MutableLiveData<String>
        get() = categoryDetail
    init {
        application = _application
        listFoodLiveData = MutableLiveData<ArrayList<Food>>()
        foodLivedata = MutableLiveData<Food>()
        categoryLiveData = MutableLiveData<ArrayList<Category>>()
        category = MutableLiveData<ArrayList<String>>()
        check = MutableLiveData<Boolean>(false)
        categoryDetail = MutableLiveData<String>()

        firebaseFirestore = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
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
                        val food = Food(
                            foodId,
                            foodName,
                            price,
                            rating,
                            time,
                            image,
                            describe,
                            bestFood,
                            adminId,
                            categoryId
                        )
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
                        val category =
                            Category(categoryId, categoryName, image)
                        listCategory.add(category)
                    }
                    categoryLiveData.postValue(listCategory)
                }
            }
            .addOnFailureListener {
                Log.d("getCategory", "Error getting category: $it")
            }
    }
    fun categoryName(){
        firebaseFirestore.collection("category")
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful && it.result != null){
                    val list = ArrayList<String>()
                    for(document in it.result){
                        val categoryName = document.getString("categoryName")
                        list.add(categoryName.toString())
                    }
                    category.postValue(list)
                }
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
                        val food = Food(
                            foodId,
                            foodName,
                            price,
                            rating,
                            time,
                            image,
                            describe,
                            bestFood,
                            adminId,
                            categoryId
                        )
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

    fun foodAdmin(id: String){
        firebaseFirestore.collection("food").whereEqualTo("adminId", id)
            .get()
            .addOnCompleteListener {task ->
                if(task.isSuccessful && task.result != null){
                    val list = ArrayList<Food>()
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
                        val food = Food(
                            foodId,
                            foodName,
                            price,
                            rating,
                            time,
                            image,
                            describe,
                            bestFood,
                            adminId,
                            categoryId)
                        list.add(food)
                    }
                    listFoodLiveData.postValue(list)
                }
            }
            .addOnFailureListener {
                Log.d("getFood", "Error getting food: $it")
            }
    }

    fun deleteFoodAdmin(foodId: String){
        firebaseFirestore.collection("food").document(foodId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(application, "Xóa thành công", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener{
                Toast.makeText(application, "Xóa không thành công", Toast.LENGTH_LONG).show()
            }
    }

    fun addFood(food: Food, image: Uri){
        val reference = storageReference.child("food_image/" + UUID.randomUUID().toString())
        reference.putFile(image).addOnSuccessListener {
            reference.downloadUrl.addOnSuccessListener { uri ->
                val newFood = HashMap<String, Any>()
                newFood["foodName"] = food.foodName.toString()
                newFood["price"] = food.price!!.toDouble()
                newFood["rating"] = food.rating!!.toDouble()
                newFood["time"] = food.time.toString()
                newFood["image"] = uri.toString()
                newFood["describe"] = food.describe.toString()
                newFood["bestFood"] = food.bestFood!!
                newFood["adminId"] = food.adminId.toString()
                newFood["categoryId"] = food.categoryId.toString()
                firebaseFirestore.collection("food")
                    .add(newFood)
                    .addOnSuccessListener {
                        check.postValue(true)
                        Log.d("addFood", "Success")
                    }
                    .addOnFailureListener {
                        Log.d("addFood", "Fail")
                    }
            }
        }.addOnFailureListener {
            Log.d("upImage", "Fail")
        }

    }

    fun getCategoryId(categoryName: String){
        firebaseFirestore.collection("category").whereEqualTo("categoryName", categoryName)
            .get()
            .addOnCompleteListener {task ->
                for(document in task.result){
                    categoryDetail.postValue(document.id)
                }
            }
    }
    fun getCategoryName(categoryId: String){
        firebaseFirestore.collection("category").whereEqualTo("categoryId", categoryId)
            .get()
            .addOnCompleteListener {task ->
                for(document in task.result){
                    categoryDetail.postValue(document.getString("categoryName"))
                }
            }
    }
    fun foodDetailAdmin(foodId: String){
        firebaseFirestore.collection("food").document(foodId)
            .get()
            .addOnSuccessListener {
                if(it.exists()){
                    val food = it.toObject(Food::class.java)
                    foodLivedata.postValue(food!!)
                }
            }
            .addOnFailureListener {
                Log.d("getFoodDetailAdmin", "Error getting food detail: $it")
            }
    }

    fun updateImage(foodId: String, image: Uri){
        val reference = storageReference.child("food_image/" + UUID.randomUUID().toString())
        reference.putFile(image).addOnSuccessListener {
            reference.downloadUrl.addOnSuccessListener { uri ->
                firebaseFirestore.collection("food").document(foodId)
                    .update("image", uri.toString())
                    .addOnSuccessListener {
                        Log.d("updateFood", "Success")
                    }
                    .addOnFailureListener {
                        Log.d("updateFood", "Fail")
                    }
            }
        }.addOnFailureListener {
            Log.d("upImage", "Fail")
        }
    }

    fun updateFoodAdmin(food: Food, foodId: String?){
        val newFood = HashMap<String, Any>()
        newFood["foodName"] = food.foodName.toString()
        newFood["price"] = food.price!!.toDouble()
        newFood["time"] = food.time.toString()
        newFood["describe"] = food.describe.toString()
        newFood["bestFood"] = food.bestFood!!
        newFood["categoryId"] = food.categoryId.toString()
        firebaseFirestore.collection("food").document(foodId.toString())
            .update(newFood)
            .addOnSuccessListener {
                check.postValue(true)
                Log.d("updateFood", "Success")
            }
            .addOnFailureListener {
                Log.d("updateFood", "Fail")
                it.printStackTrace()
            }
    }
}