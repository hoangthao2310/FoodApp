package com.example.foodapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodapp.R
import com.example.foodapp.base.BaseActivity
import com.example.foodapp.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity<ActivityIntroBinding>() {
    override fun initViews() {

    }

    override fun getLayout(): ActivityIntroBinding = ActivityIntroBinding.inflate(layoutInflater)
}