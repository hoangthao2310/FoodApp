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
    private var checkAdd: MutableLiveData<Boolean>
    private var total: MutableLiveData<Double>
    private var cartId: MutableLiveData<ArrayList<String>>
    private var orderLiveData: MutableLiveData<ArrayList<Order>>

    val getCartFirebase: MutableLiveData<ArrayList<Cart>>
        get() = cartLiveData
    val isCheck: MutableLiveData<Boolean>
        get() = checkAdd
    val totalPrice: MutableLiveData<Double>
        get() = total
    val getCartId: MutableLiveData<ArrayList<String>>
        get() = cartId
    val getOrderFirebase: MutableLiveData<ArrayList<Order>>
        get() = orderLiveData
    val getCartAdmin: MutableLiveData<ArrayList<CartAdmin>>
        get() = cartAdminLiveData
    init {
        cartRepository = CartRepository(application)
        checkAdd = cartRepository.isCheckAddCart
        cartLiveData = cartRepository.getCartFirebase
        total = cartRepository.totalPriceCart
        cartId = cartRepository.cartId
        orderLiveData = cartRepository.getOrderFirebase
        cartAdminLiveData = cartRepository.getCartAdminFirebase
    }
    fun addCartAdmin(adminId: String, userName: String, quantityFood: Int){
        cartRepository.addCartAdmin(adminId, userName, quantityFood)
    }
    fun getCartAdmin(){
        cartRepository.getCartAdmin()
    }

    fun addCart(food: Food, quantity: Int, intoMoney: Double){
        cartRepository.addCart(food, quantity, intoMoney)
    }
    fun getCart(adminId: String){
        cartRepository.getCart(adminId)
    }
    fun getCartId(){
        cartRepository.getIdCart()
    }
    fun updateQuantity(cart: Cart, adminId: String){
        cartRepository.updateQuantity(cart, adminId)
    }
    fun totalPrice(adminId: String){
        cartRepository.totalPrice(adminId)
    }
    fun addOrder(order: Order){
        cartRepository.addOrder(order)
    }
    fun getOrder(){
        cartRepository.getOrder()
    }
    fun deleteCart(foodId: String){
        cartRepository.deleteCart(foodId)
    }
}