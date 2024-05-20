package com.example.foodapp.repository

import android.app.Application
import android.content.ContentValues
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.foodapp.model.Category
import com.example.foodapp.model.Food
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID
import kotlin.random.Random

class FoodRepository(_application: Application) {
    private var listFoodLiveData: MutableLiveData<ArrayList<Food>>
    private var categoryLiveData: MutableLiveData<ArrayList<Category>>
    private var application: Application

    private val storageReference: StorageReference
    private var database: FirebaseDatabase

    val getFoodFirebase: MutableLiveData<ArrayList<Food>>
        get() = listFoodLiveData
    val getCategoryFirebase: MutableLiveData<ArrayList<Category>>
        get() = categoryLiveData
    init {
        application = _application
        listFoodLiveData = MutableLiveData<ArrayList<Food>>()
        categoryLiveData = MutableLiveData<ArrayList<Category>>()

        storageReference = FirebaseStorage.getInstance().reference
        database = FirebaseDatabase.getInstance()
    }

    fun bestFood(){
        database.getReference("Foods").orderByChild("bestFood").equalTo(true)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<Food>()
                    for(itemSnap in snapshot.children){
                        val food = itemSnap.getValue(Food::class.java)
                        if (food != null) {
                            list.add(food)
                        }
                    }
                    listFoodLiveData.postValue(list)
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read best food.", error.toException())
                }

            })
    }

    fun category(){
        database.getReference("Category").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<Category>()
                for(snap in snapshot.children){
                    val category = snap.getValue(Category::class.java)
                    if (category != null) {
                        list.add(category)
                    }
                }
                categoryLiveData.postValue(list)
                Log.d("category", list.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("category", "Failed to read value.", error.toException())
            }

        })
    }

    fun food(cateId: String?){
        database.getReference("Foods").orderByChild("categoryId").equalTo(cateId)
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<Food>()
                for(snap in snapshot.children){
                    val food = snap.getValue(Food::class.java)
                    if (food != null) {
                        list.add(food)
                    }
                }
                listFoodLiveData.postValue(list)
                Log.d("Food", list.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Food", "Failed to read value.", error.toException())
            }

        })
    }

    fun getAllFood(){
        database.getReference("Foods")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<Food>()
                    for(snap in snapshot.children){
                        val food = snap.getValue(Food::class.java)
                        if (food != null) {
                            list.add(food)
                        }
                    }
                    listFoodLiveData.postValue(list)
                    Log.d("Food", list.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("Food", "Failed to read value.", error.toException())
                }

            })
    }

    fun foodAdmin(id: String){
        database.getReference("Foods").orderByChild("adminId").equalTo(id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<Food>()
                    for(snap in snapshot.children){
                        val food = snap.getValue(Food::class.java)
                        if (food != null) {
                            list.add(food)
                        }
                    }
                    listFoodLiveData.postValue(list)
                    Log.d("Food Admin", list.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("Food Admin", "Failed to read value.", error.toException())
                }

            })
    }

    fun deleteFoodAdmin(foodId: String){
        database.getReference("Foods").child(foodId)
            .removeValue()
            .addOnCompleteListener {
                Log.d("deleteFoodAdmin", "DocumentSnapshot successfully deleted!")
                Toast.makeText(application, "Xóa thành công", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(application, "Xóa không thành công", Toast.LENGTH_LONG).show()
            }
    }

    fun addFood(food: Food, image: Uri?){
        val id = Random.nextInt(100000)
        val reference = storageReference.child("food_image/" + UUID.randomUUID().toString())
        if (image != null) {
            reference.putFile(image).addOnSuccessListener {
                reference.downloadUrl.addOnSuccessListener { uri ->
                    val newFood = HashMap<String, Any>()
                    newFood["foodId"] = id.toString()
                    newFood["foodName"] = food.foodName.toString()
                    newFood["price"] = food.price!!.toDouble()
                    newFood["rating"] = food.rating!!.toDouble()
                    newFood["time"] = food.time.toString()
                    newFood["image"] = uri.toString()
                    newFood["describe"] = food.describe.toString()
                    newFood["bestFood"] = food.bestFood!!
                    newFood["adminId"] = food.adminId.toString()
                    newFood["categoryId"] = food.categoryId.toString()
                    database.getReference("Foods").child(id.toString())
                        .setValue(newFood)
                        .addOnSuccessListener {
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

    }

    fun updateImage(foodId: String, image: Uri?){
        val reference = storageReference.child("food_image/" + UUID.randomUUID().toString())
        if (image != null) {
            reference.putFile(image).addOnSuccessListener {
                reference.downloadUrl.addOnSuccessListener { uri ->
                    database.getReference("Foods").child(foodId)
                        .setValue("image", uri.toString())
                        .addOnSuccessListener {
                            Log.d("updateImageFood", "Success")
                        }
                        .addOnFailureListener {
                            Log.d("updateImageFood", "Fail")
                        }
                }
            }.addOnFailureListener {
                Log.d("upImage", "Fail")
            }
        }
    }

    fun updateFoodAdmin(food: Food, foodId: String){
        val newFood = HashMap<String, Any>()
        newFood["foodName"] = food.foodName.toString()
        newFood["price"] = food.price!!.toDouble()
        newFood["time"] = food.time.toString()
        newFood["describe"] = food.describe.toString()
        newFood["bestFood"] = food.bestFood!!
        newFood["categoryId"] = food.categoryId.toString()
        database.getReference("Foods").child(foodId)
            .updateChildren(newFood)
            .addOnSuccessListener {
                Log.d("updateFood", "Success")
            }
            .addOnFailureListener {
                Log.d("updateFood", "Fail")
                it.printStackTrace()
            }
    }
}