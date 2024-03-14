package com.example.foodapp

interface OnActionCallback {
    fun backToPrevious()

    fun showFragment(fromFragment: Class<*>, toFragment: Class<*>, enterAnim: Int, exitAnim: Int, data: Any? = null, isBack: Boolean = false)
}