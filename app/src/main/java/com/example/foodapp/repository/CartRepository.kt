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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.random.Random

class CartRepository(_application: Application) {
    private var cartLiveData: MutableLiveData<ArrayList<Cart>>
    private var cartAdminLiveData: MutableLiveData<ArrayList<CartAdmin>>
    private var cartIdLiveData: MutableLiveData<ArrayList<String>>
    private var orderLiveData: MutableLiveData<ArrayList<Order>>

    private val application: Application
    private val auth: FirebaseAuth
    private var database: FirebaseDatabase

    val getCartFirebase: MutableLiveData<ArrayList<Cart>>
        get() = cartLiveData
    val cartId: MutableLiveData<ArrayList<String>>
        get() = cartIdLiveData
    val getOrderFirebase: MutableLiveData<ArrayList<Order>>
        get() = orderLiveData
    val getCartAdminFirebase: MutableLiveData<ArrayList<CartAdmin>>
        get() = cartAdminLiveData
    init {
        application = _application
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        cartIdLiveData = MutableLiveData<ArrayList<String>>()
        cartLiveData = MutableLiveData<ArrayList<Cart>>()
        orderLiveData = MutableLiveData<ArrayList<Order>>()
        cartAdminLiveData = MutableLiveData<ArrayList<CartAdmin>>()
    }

    fun addCartAdmin(adminId: String, userName: String, foodName: String){
        val addNew: HashMap<String, Any> = HashMap()
        addNew["adminId"] = adminId
        addNew["userName"] = userName
        addNew["foodName"] = foodName
        addNew["userId"] = auth.currentUser!!.uid
        database.getReference("CartAdmin").child(adminId)
            .setValue(addNew)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "addCartAdmin Success")
            }
            .addOnFailureListener {
                Log.w(ContentValues.TAG, "Error adding cartAdmin", it)
            }
    }

    fun getCartAdmin(){
        database.getReference("CartAdmin").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<CartAdmin>()
                for(snap in snapshot.children){
                    val cartAdmin = snap.getValue(CartAdmin::class.java)
                    if (cartAdmin != null) {
                        list.add(cartAdmin)
                    }
                }
                cartAdminLiveData.postValue(list)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("getCartAdmin", "Failed to read value.", error.toException())
            }

        })
    }

    fun addCartDetail(food: Food, quantity: Int, intoMoney: Double){
        val cart:HashMap<String, Any> = HashMap()
        cart["foodId"] = food.foodId.toString()
        cart["foodName"] = food.foodName.toString()
        cart["price"] = food.price!!.toDouble()
        cart["image"] = food.image.toString()
        cart["quantity"] = quantity
        cart["intoMoney"] = intoMoney
        cart["adminId"] = food.adminId.toString()
        database.getReference("CartDetail").child(food.foodId.toString())
            .setValue(cart)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "addCartDetail Success")
            }
            .addOnFailureListener {e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }

    fun getCartDetail(adminId: String){
        database.getReference("CartDetail").orderByChild("adminId").equalTo(adminId)
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<Cart>()
                for(snap in snapshot.children){
                    val cart = snap.getValue(Cart::class.java)
                    if (cart != null) {
                        list.add(cart)
                    }
                }
                cartLiveData.postValue(list)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("getCartDetail", "Failed to read value.", error.toException())
            }

        })
    }

    fun updateQuantity(cart: Cart){
        val updateCart:HashMap<String, Any> = HashMap()
        updateCart["quantity"] = cart.quantity!!
        updateCart["intoMoney"] = cart.quantity!! * cart.price!!
        database.getReference("CartDetail").child(cart.foodId.toString())
            .updateChildren(updateCart)
            .addOnSuccessListener {
                Log.d("Edit", "Quantity updated successfully!")
            }
            .addOnFailureListener {
                Log.e("Edit", "Quantity updating error", it)
            }
    }

    fun addOrder(order: Order){
        val id = Random.nextInt(10000)
        val newOrder:HashMap<String, Any> = HashMap()
        newOrder["orderId"] = id.toString()
        newOrder["userName"] = order.userName.toString()
        newOrder["phoneNumber"] = order.phoneNumber.toString()
        newOrder["totalPrice"] = order.totalPrice!!.toDouble()
        newOrder["address"] = order.address.toString()
        newOrder["deliveryMethods"] = order.deliveryMethods.toString()
        newOrder["paymentMethods"] = order.paymentMethods.toString()
        newOrder["describeOrder"] = order.describeOrder.toString()
        newOrder["orderStatus"] = order.orderStatus.toString()
        newOrder["userId"] = order.userId.toString()
        newOrder["adminId"] = order.adminId.toString()
        database.getReference("Orders").child(id.toString())
            .setValue(newOrder)
            .addOnCompleteListener {
                Log.d(ContentValues.TAG, "Order success")
            }
            .addOnFailureListener {e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }

    fun updateOrder(orderId: String, orderStatus: String){
        val newOrder:HashMap<String, Any> = HashMap()
        newOrder["orderStatus"] = orderStatus
        database.getReference("Orders").child(orderId)
            .updateChildren(newOrder)
            .addOnSuccessListener {
                Toast.makeText(application, "Cập nhật đơn hàng thành công", Toast.LENGTH_LONG).show()
                Log.d(ContentValues.TAG, "Update order status success")
            }
            .addOnFailureListener {e ->
                Toast.makeText(application, "Cập nhật đơn hàng thất bại", Toast.LENGTH_LONG).show()
                Log.w(ContentValues.TAG, "Error update document", e)
            }
    }

    fun getOrder(id: String){
        database.getReference("Orders").orderByChild("userId").equalTo(id)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<Order>()
                    for(itemSnap in snapshot.children){
                        val order = itemSnap.getValue(Order::class.java)
                        order?.let { list.add(it) }
                    }
                    orderLiveData.postValue(list)
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read order.", error.toException())
                }

            })
    }

    fun getOrderAdmin(id: String){
        database.getReference("Orders").orderByChild("adminId").equalTo(id)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<Order>()
                    for(itemSnap in snapshot.children){
                        val order = itemSnap.getValue(Order::class.java)
                        order?.let { list.add(it) }
                    }
                    orderLiveData.postValue(list)
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read order.", error.toException())
                }

            })
    }

    fun deleteCartAdmin(cartAdminId: String){
        database.getReference("CartAdmin").child(cartAdminId)
            .removeValue()
            .addOnCompleteListener {
                Log.d("deleteCartAdmin", "DocumentSnapshot successfully deleted!")
            }
            .addOnFailureListener {
                Log.d("deleteCartAdmin", "DocumentSnapshot fail deleted!")
            }
    }

    fun deleteCartDetail(cartDetailId: String){
        database.getReference("CartDetail").child(cartDetailId)
            .removeValue()
            .addOnCompleteListener {
                Log.d("deleteCartDetail", "DocumentSnapshot successfully deleted!")
            }
            .addOnFailureListener {
            }
    }

}