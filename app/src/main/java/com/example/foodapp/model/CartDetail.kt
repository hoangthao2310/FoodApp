package com.example.foodapp.model

data class CartDetail(
    val cartId: String?,
    val foodId: String?,
    val phoneNumber: String?,
    val totalAmount: Double?,
    val address: String?,
    val deliveryMethods: String?,
    val paymentMethods: String?
)
