package com.example.foodapp.repository

import android.app.Application
import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.foodapp.model.Cart
import com.example.foodapp.model.CartAdmin
import com.example.foodapp.model.Food
import com.example.foodapp.model.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CartRepository(_application: Application) {
    private var cartLiveData: MutableLiveData<ArrayList<Cart>>
    private var cartAdminLiveData: MutableLiveData<ArrayList<CartAdmin>>
    private var checkAddFirebase: MutableLiveData<Boolean>
    private var totalPriceLiveData: MutableLiveData<Double>
    private var cartIdLiveData: MutableLiveData<ArrayList<String>>
    private var orderLiveData: MutableLiveData<ArrayList<Order>>

    private val application: Application
    private val firebaseFirestore: FirebaseFirestore
    private val auth: FirebaseAuth

    val getCartFirebase: MutableLiveData<ArrayList<Cart>>
        get() = cartLiveData
    val isCheckAddCart: MutableLiveData<Boolean>
        get() = checkAddFirebase
    val totalPriceCart: MutableLiveData<Double>
        get() = totalPriceLiveData
    val cartId: MutableLiveData<ArrayList<String>>
        get() = cartIdLiveData
    val getOrderFirebase: MutableLiveData<ArrayList<Order>>
        get() = orderLiveData
    val getCartAdminFirebase: MutableLiveData<ArrayList<CartAdmin>>
        get() = cartAdminLiveData
    init {
        application = _application
        firebaseFirestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        checkAddFirebase = MutableLiveData<Boolean>(false)
        totalPriceLiveData = MutableLiveData<Double>()
        cartIdLiveData = MutableLiveData<ArrayList<String>>()
        cartLiveData = MutableLiveData<ArrayList<Cart>>()
        orderLiveData = MutableLiveData<ArrayList<Order>>()
        cartAdminLiveData = MutableLiveData<ArrayList<CartAdmin>>()
    }

    fun addCartAdmin(adminId: String, userName: String, quantityFood: Int){
        val addNew: HashMap<String, Any> = HashMap()
        addNew["userName"] = userName
        addNew["quantityFood"] = quantityFood
        firebaseFirestore.collection("users").document(auth.currentUser!!.uid)
            .collection("cart").document(adminId)
            .set(addNew)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Success")
            }
            .addOnFailureListener {
                Log.w(ContentValues.TAG, "Error adding document", it)
            }
    }

    fun getCartAdmin(){
        firebaseFirestore.collection("users").document(auth.currentUser!!.uid)
            .collection("cart")
            .get()
            .addOnCompleteListener { task ->
                if(task.isSuccessful && task.result != null){
                    val list = ArrayList<CartAdmin>()
                    for(document in task.result){
                        val adminId = document.id
                        val userName = document.getString("userName")
                        val quantityFood = document.getLong("quantityFood")!!.toInt()
                        val cartAdmin = CartAdmin(adminId, userName, quantityFood)
                        list.add(cartAdmin)
                    }
                    cartAdminLiveData.postValue(list)
                }
            }
            .addOnFailureListener {
                Log.d("getCartAdmin", "Error getting cart: $it")
            }
    }

    fun addCart(food: Food, quantity: Int, intoMoney: Double){
        val cart:HashMap<String, Any> = HashMap()
        cart["foodId"] = food.foodId.toString()
        cart["adminId"] = food.adminId.toString()
        cart["foodName"] = food.foodName.toString()
        cart["price"] = food.price!!.toDouble()
        cart["image"] = food.image.toString()
        cart["quantity"] = quantity
        cart["intoMoney"] = intoMoney
        firebaseFirestore.collection("users").document(auth.currentUser!!.uid)
            .collection("cart").document(food.adminId.toString()).collection("cartDetail")
            .add(cart)
            .addOnSuccessListener { documentReference ->
                checkAddFirebase.postValue(true)
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener {e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }

    fun getCart(adminId: String){
        firebaseFirestore.collection("users").document(auth.currentUser!!.uid)
            .collection("cart").document(adminId).collection("cartDetail")
            .get()
            .addOnCompleteListener { task ->
                if(task.isSuccessful && task.result != null){
                    val listFoodCart = ArrayList<Cart>()
                    for(document in task.result){
                        val cartId = document.id
                        val foodId = document.getString("foodId")
                        val foodName = document.getString("foodName")
                        val price = document.getDouble("price")
                        val image = document.getString("image")
                        val quantity = document.getLong("quantity")!!.toInt()
                        val intoMoney = document.getDouble("intoMoney")
                        val cart = Cart(
                            cartId,
                            foodId,
                            foodName,
                            price,
                            quantity,
                            image,
                            intoMoney
                        )
                        listFoodCart.add(cart)
                    }
                    cartLiveData.postValue(listFoodCart)
                }
            }
            .addOnFailureListener {
                Log.d("getCart", "Error getting cart: $it")
            }
    }

    fun getIdCart(){
        firebaseFirestore.collection("users").document(auth.currentUser!!.uid).collection("cart")
            .get()
            .addOnCompleteListener { task ->
                if(task.isSuccessful && task.result != null){
                    val listId = ArrayList<String>()
                    for(document in task.result){
                        val id = document.id
                        listId.add(id)
                    }
                    cartIdLiveData.postValue(listId)
                }
            }
            .addOnFailureListener {
                Log.d("getFoodId", "Error getting cart: $it")
            }
    }

    fun updateQuantity(cart: Cart, adminId: String){
        val updateCart:HashMap<String, Any> = HashMap()
        updateCart["quantity"] = cart.quantity!!
        updateCart["intoMoney"] = cart.quantity!! * cart.price!!
        firebaseFirestore.collection("users").document(auth.currentUser!!.uid)
            .collection("cart").document(adminId).collection("cartDetail").document(cart.cartId.toString())
            .update(updateCart)
            .addOnSuccessListener {
                Log.d("Edit", "Quantity updated successfully!")
            }
            .addOnFailureListener {
                Log.e("Edit", "Quantity updating error", it)
            }
    }

    fun totalPrice(adminId: String){
        var sum = 0.0
        firebaseFirestore.collection("users").document(auth.currentUser!!.uid)
            .collection("cart").document(adminId).collection("cartDetail")
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

    fun addOrder(order: Order){
        val newOrder:HashMap<String, Any> = HashMap()
        newOrder["userName"] = order.userName.toString()
        newOrder["phoneNumber"] = order.phoneNumber.toString()
        newOrder["address"] = order.address.toString()
        newOrder["deliveryMethods"] = order.deliveryMethods.toString()
        newOrder["paymentMethods"] = order.paymentMethods.toString()
        newOrder["totalPrice"] = order.totalPrice!!.toDouble()
        newOrder["describeOrder"] = order.describeOrder.toString()
        newOrder["orderStatus"] = order.orderStatus.toString()
        newOrder["adminId"] = order.adminId.toString()
        firebaseFirestore.collection("users").document(auth.currentUser!!.uid)
            .collection("order").add(newOrder)
            .addOnSuccessListener { documentReference ->
                checkAddFirebase.postValue(true)
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener {e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }

    fun getOrder(){
        firebaseFirestore.collection("users").document(auth.currentUser!!.uid).collection("order")
            .get()
            .addOnCompleteListener { task ->
                if(task.isSuccessful && task.result != null){
                    val list = ArrayList<Order>()
                    for(document in task.result){
                        val orderId = document.id
                        val userName = document.getString("userName")
                        val phoneNumber = document.getString("phoneNumber")
                        val totalPrice = document.getDouble("totalPrice")
                        val address = document.getString("address")
                        val deliveryMethods = document.getString("deliveryMethods")
                        val paymentMethods = document.getString("paymentMethods")
                        val describeOrder = document.getString("describeOrder")
                        val orderStatus = document.getString("orderStatus")
                        val adminId = document.getString("adminId")
                        val order = Order(orderId, userName, phoneNumber, totalPrice, address, deliveryMethods, paymentMethods, describeOrder, orderStatus, adminId)
                        list.add(order)
                    }
                    orderLiveData.postValue(list)
                    Log.d("getOrder", list.toString())
                }
            }
            .addOnFailureListener {
                Log.d("getOrder", "Error getting cart: $it")
            }
    }

    fun deleteCart(cartId: String){
        firebaseFirestore.collection("users").document(auth.currentUser!!.uid)
            .collection("cart").document(cartId)
            .delete()
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Delete cart successfully")
            }
            .addOnFailureListener {
                Log.d(ContentValues.TAG, "Delete cart fail")
            }
    }

}