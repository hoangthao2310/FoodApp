package com.example.foodapp.model

data class Order(
    val foodId: String?,
    val userName: String?,
    val phoneNumber: String?,
    var totalPrice: Double?,
    val address: String?,
    val deliveryMethods: String?,
    val paymentMethods: String?
)
