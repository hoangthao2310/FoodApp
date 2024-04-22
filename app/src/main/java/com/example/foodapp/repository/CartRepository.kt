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
    private var checkAddFirebase: MutableLiveData<Boolean>
    private var totalPriceLiveData: MutableLiveData<Double>

    private val application: Application
    private val firebaseFirestore: FirebaseFirestore
    private val auth: FirebaseAuth

    val getCartFirebase: MutableLiveData<ArrayList<Cart>>
        get() = cartLiveData
    val isCheckAddCart: MutableLiveData<Boolean>
        get() = checkAddFirebase
    val totalPriceCart: MutableLiveData<Double>
        get() = totalPriceLiveData
    init {
        application = _application
        firebaseFirestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        checkAddFirebase = MutableLiveData<Boolean>(false)
        totalPriceLiveData = MutableLiveData<Double>()
        cartLiveData = MutableLiveData<ArrayList<Cart>>()
    }

    fun addCart(food: Food, quantity: Int, intoMoney: Double){
        val cart:HashMap<String, Any> = HashMap()
        cart["foodId"] = food.foodId.toString()
        cart["foodName"] = food.foodName.toString()
        cart["price"] = food.price!!.toDouble()
        cart["image"] = food.image.toString()
        cart["quantity"] = quantity
        cart["intoMoney"] = intoMoney
        firebaseFirestore.collection("users").document(auth.currentUser!!.uid)
            .collection("cart").add(cart)
            .addOnSuccessListener { documentReference ->
                checkAddFirebase.postValue(true)
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
                        val intoMoney = document.getDouble("intoMoney")
                        val cart = Cart(foodId, foodName, price, quantity, image, intoMoney)
                        listFoodCart.add(cart)
                    }
                    cartLiveData.postValue(listFoodCart)
                }
            }
            .addOnFailureListener {
                Log.d("getCart", "Error getting cart: $it")
            }
    }

    fun updateQuantity(cart: Cart){
        val updateCart:HashMap<String, Any> = HashMap()
        updateCart["quantity"] = cart.quantity!!
        updateCart["intoMoney"] = cart.quantity!! * cart.price!!
        firebaseFirestore.collection("users").document(auth.currentUser!!.uid)
            .collection("cart").document(cart.foodId.toString())
            .update(updateCart)
            .addOnSuccessListener {
                Log.d("Edit", "Quantity updated successfully!")
            }
            .addOnFailureListener {
                Log.e("Edit", "Quantity updating error", it)
            }
    }

    fun totalPrice(){
        var sum = 0.0
        firebaseFirestore.collection("users").document(auth.currentUser!!.uid)
            .collection("cart")
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful && it.result != null){
                    for(document in it.result){
                        val intoMoney = document.getDouble("intoMoney")
                        if (intoMoney != null) {
                            sum += intoMoney
                        }
                        totalPriceLiveData.postValue(sum)
                    }
                }
            }
    }

    fun order(totalPrice: Double){
        val order:HashMap<String, Any> = HashMap()
        order["totalPrice"] = totalPrice
        firebaseFirestore.collection("users").document(auth.currentUser!!.uid)
            .collection("order").add(order)
            .addOnSuccessListener { documentReference ->
                checkAddFirebase.postValue(true)
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener {e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }
}