package com.example.foodapp.model

data class Food(
    val foodName: String? = null,
    val price: Double? = null,
    val rating: Double? = null,
    val time: String? = null,
    val image: String? = null,
    val bestFood: Boolean? = null,
    val adminId: String? = null,
    val categoryId: String? = null
)
