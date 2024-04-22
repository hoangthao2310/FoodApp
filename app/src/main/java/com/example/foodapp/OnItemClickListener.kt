package com.example.foodapp

interface OnItemClickListener {
    fun onItemClick(data: Any?)

    fun onItemAddClick(data: Any?)
    fun onItemEditClick(data: Any?)
    fun onItemDeleteClick(data: Any?)
}