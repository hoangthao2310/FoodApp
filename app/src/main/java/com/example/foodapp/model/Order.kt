package com.example.foodapp.model

data class Order(
    val orderId: String? = null,
    val userName: String?= null,
    val phoneNumber: String?= null,
    var totalPrice: Double?= null,
    val address: String?= null,
    val deliveryMethods: String?= null,
    val paymentMethods: String?= null,
    val describeOrder: String?= null,
    val orderStatus: String?= null,
    val userId: String?= null,
    val adminId: String?= null,
)
