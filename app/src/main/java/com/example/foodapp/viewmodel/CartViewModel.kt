package com.example.foodapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.foodapp.model.Cart
import com.example.foodapp.model.CartAdmin
import com.example.foodapp.model.Food
import com.example.foodapp.model.Order
import com.example.foodapp.repository.CartRepository

class CartViewModel(application: Application): AndroidViewModel(application) {
    private val cartRepository: CartRepository
    private val cartLiveData: MutableLiveData<ArrayList<Cart>>
    private val cartAdminLiveData: MutableLiveData<ArrayList<CartAdmin>>
    private var cartId: MutableLiveData<ArrayList<String>>
    private var orderLiveData: MutableLiveData<ArrayList<Order>>

    val getCartFirebase: MutableLiveData<ArrayList<Cart>>
        get() = cartLiveData
    val getOrderFirebase: MutableLiveData<ArrayList<Order>>
        get() = orderLiveData
    val getCartAdmin: MutableLiveData<ArrayList<CartAdmin>>
        get() = cartAdminLiveData
    init {
        cartRepository = CartRepository(application)
        cartLiveData = cartRepository.getCartFirebase
        cartId = cartRepository.cartId
        orderLiveData = cartRepository.getOrderFirebase
        cartAdminLiveData = cartRepository.getCartAdminFirebase
    }
    fun addCartAdmin(adminId: String, userName: String, foodName: String){
        cartRepository.addCartAdmin(adminId, userName, foodName)
    }
    fun getCartAdmin(userId: String){
        cartRepository.getCartAdmin(userId)
    }

    fun addCartDetail(food: Food, quantity: Int, intoMoney: Double){
        cartRepository.addCartDetail(food, quantity, intoMoney)
    }
    fun getCartDetail(cartAdminId: String){
        cartRepository.getCartDetail(cartAdminId)
    }
    fun updateQuantity(cart: Cart){
        cartRepository.updateQuantity(cart)
    }
    fun addOrder(order: Order){
        cartRepository.addOrder(order)
    }
    fun getOrder(id: String){
        cartRepository.getOrder(id)
    }
    fun getOrderAdmin(id: String){
        cartRepository.getOrderAdmin(id)
    }
    fun updateOrder(orderId: String, orderStatus: String){
        cartRepository.updateOrder(orderId, orderStatus)
    }
    fun deleteCartAdmin(cartAdminId: String){
        cartRepository.deleteCartAdmin(cartAdminId)
    }
    fun deleteCartDetail(cartDetailId: String){
        cartRepository.deleteCartDetail(cartDetailId)
    }
}