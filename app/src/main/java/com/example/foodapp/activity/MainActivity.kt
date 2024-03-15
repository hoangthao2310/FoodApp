package com.example.foodapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodapp.R
import com.example.foodapp.base.BaseActivity
import com.example.foodapp.databinding.ActivityMainBinding
import com.example.foodapp.view.LoginFragment

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    override fun initViews() {
        showFragment(this::class.java, LoginFragment::class.java, 0, 0)
    }

    override fun getLayout(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
}