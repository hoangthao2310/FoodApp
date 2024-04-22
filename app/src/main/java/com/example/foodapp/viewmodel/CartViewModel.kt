package com.example.foodapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.foodapp.model.Cart
import com.example.foodapp.model.Food
import com.example.foodapp.repository.CartRepository

class CartViewModel(application: Application): AndroidViewModel(application) {
    private val cartRepository: CartRepository
    private val cartLiveData: MutableLiveData<ArrayList<Cart>>
    private var checkAdd: MutableLiveData<Boolean>
    private var total: MutableLiveData<Double>

    val getCartFirebase: MutableLiveData<ArrayList<Cart>>
        get() = cartLiveData
    val isCheck: MutableLiveData<Boolean>
        get() = checkAdd
    val totalPrice: MutableLiveData<Double>
        get() = total

    init {
        cartRepository = CartRepository(application)
        checkAdd = cartRepository.isCheckAddCart
        cartLiveData = cartRepository.getCartFirebase
        total = cartRepository.totalPriceCart
    }

    fun addCart(food: Food, quantity: Int, intoMoney: Double){
        cartRepository.addCart(food, quantity, intoMoney)
    }
    fun getCart(){
        cartRepository.getCart()
    }
    fun updateCart(cart: Cart){
        cartRepository.updateQuantity(cart)
    }
    fun totalPrice(){
        cartRepository.totalPrice()
    }
    fun order(totalPrice: Double){
        cartRepository.order(totalPrice)
    }
}