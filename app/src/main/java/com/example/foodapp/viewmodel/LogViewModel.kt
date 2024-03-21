package com.example.foodapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.foodapp.model.User
import com.example.foodapp.repository.LogRepository
import com.google.firebase.auth.FirebaseUser

class LogViewModel(application: Application) : AndroidViewModel(application) {
    private var logRepository: LogRepository
    private var user: MutableLiveData<User>
    private var userFirebase: MutableLiveData<FirebaseUser>
    private var logStatus: MutableLiveData<Boolean>

    val getUserData: MutableLiveData<FirebaseUser>
        get() = userFirebase
    val getUser: MutableLiveData<User>
        get() = user
    val getLogStatus: MutableLiveData<Boolean>
        get() = logStatus
    init {
        logRepository = LogRepository(application)
        user = logRepository.getUser
        userFirebase = logRepository.getFirebaseUser
        logStatus = logRepository.isCheckLog
    }

    fun register(email: String, password: String, name: String){
        logRepository.register(email, password, name)
    }

    fun login(email: String, password: String){
        logRepository.login(email, password)
    }
}