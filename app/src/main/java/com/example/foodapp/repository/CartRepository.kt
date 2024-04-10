package com.example.foodapp.repository

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.foodapp.model.Cart
import com.example.foodapp.model.Food
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CartRepository(_application: Application) {
    private var cartLiveData: MutableLiveData<ArrayList<Cart>>
    private var checkAddCart: MutableLiveData<Boolean>

    private val application: Application
    private val firebaseFirestore: FirebaseFirestore
    private val auth: FirebaseAuth

    val getCartFirebase: MutableLiveData<ArrayList<Cart>>
        get() = cartLiveData
    val isCheckAddCart: MutableLiveData<Boolean>
        get() = checkAddCart
    init {
        application = _application
        firebaseFirestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        checkAddCart = MutableLiveData<Boolean>(false)
        cartLiveData = MutableLiveData<ArrayList<Cart>>()
    }

    fun addCart(food: Food, quantity: Int){
        val cart:HashMap<String, Any> = HashMap()
        cart["foodName"] = food.foodName.toString()
        cart["price"] = food.price!!.toDouble()
        cart["image"] = food.image.toString()
        cart["quantity"] = quantity
        firebaseFirestore.collection("users").document(auth.currentUser!!.uid)
            .collection("cart").add(cart)
            .addOnSuccessListener { documentReference ->
                checkAddCart.postValue(true)
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener {e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }

    fun getCart(){
        firebaseFirestore.collection("users").document(auth.currentUser!!.uid).collection("cart")
            .get()
            .addOnCompleteListener { task ->
                if(task.isSuccessful && task.result != null){
                    val listFoodCart = ArrayList<Cart>()
                    for(document in task.result){
                        val foodId = document.id
                        val foodName = document.getString("foodName")
                        val price = document.getDouble("price")
                        val image = document.getString("image")
                        val quantity = document.getLong("quantity")!!.toInt()
                        val cart = Cart(foodId, foodName, price, quantity, image)
                        listFoodCart.add(cart)
                    }
                    cartLiveData.postValue(listFoodCart)
                }
            }
            .addOnFailureListener {
                Log.d("getCart", "Error getting cart: $it")
            }
    }

    fun updateQuantity(cart: Cart, inputQuantity: Int){
        firebaseFirestore.collection("users").document(auth.currentUser!!.uid)
            .collection("cart").document(cart.foodId.toString())
            .set(inputQuantity)
            .addOnSuccessListener {
                Log.d("Edit", "Quantity updated successfully!")
            }
            .addOnFailureListener {
                Log.e("Edit", "Quantity updating error", it)
            }
    }
}