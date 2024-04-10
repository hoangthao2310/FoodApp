package com.example.foodapp.model

data class Cart(
    val foodId: String?,
    val foodName: String?,
    val price: Double?,
    var quantity: Int,
    val image: String?
)