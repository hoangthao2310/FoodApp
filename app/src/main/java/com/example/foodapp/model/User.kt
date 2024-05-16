package com.example.foodapp.model

data class User (
    val userId: String? = null,
    val userName: String? = null,
    val email: String? = null,
    val password: String? = null,
    val imageUser: String? = null,
    val checkAdmin: Boolean? = null,
    val address: String? = null,
    val note: String? = null,
    val contactPersonName: String? = null,
    val contactPhoneNumber: String? = null
)