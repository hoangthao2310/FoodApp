package com.example.foodapp.model

data class FoodOrdered(
    val orderId: String? = null,
    val foodName: String? = null,
    val price: Double? = null,
    var quantity: Int? = null
)
