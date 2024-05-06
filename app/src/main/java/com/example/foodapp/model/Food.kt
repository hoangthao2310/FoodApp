package com.example.foodapp.model

data class Food(
    val foodId: String? = null,
    val foodName: String? = null,
    val price: Double? = null,
    val rating: Double? = null,
    val time: String? = null,
    val image: String? = null,
    val describe: String? = null,
    val bestFood: Boolean? = null,
    val adminId: String? = null,
    val categoryId: String? = null,
    val favouriteId: String? = null
)
