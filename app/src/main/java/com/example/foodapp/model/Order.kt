package com.example.foodapp.model

data class Order(
    val orderId: String?,
    val userName: String?,
    val phoneNumber: String?,
    var totalPrice: Double?,
    val address: String?,
    val deliveryMethods: String?,
    val paymentMethods: String?,
    val describeOrder: String?,
    val orderStatus: String?,
    val adminId: String?
)
