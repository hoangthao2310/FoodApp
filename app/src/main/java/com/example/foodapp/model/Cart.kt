package com.example.foodapp.model

data class Cart(
    val foodId: String? = null,
    val foodName: String? = null,
    val price: Double? = null,
    var quantity: Int? = null,
    val image: String? = null,
    var intoMoney: Double? = null,
    val adminId: String? = null,
    val cartAdminId: String? = null
)
