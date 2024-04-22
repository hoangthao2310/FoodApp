package com.example.foodapp.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.foodapp.model.Location
import com.example.foodapp.model.User
import com.example.foodapp.repository.AccountRepository
import com.google.firebase.auth.FirebaseUser

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    private var accountRepository: AccountRepository
    private var user: MutableLiveData<User>
    private var location: MutableLiveData<ArrayList<Location>>
    private var userFirebase: MutableLiveData<FirebaseUser>
    private var logStatus: MutableLiveData<Boolean>
    private var locationStatus: MutableLiveData<Boolean>

    val getUserData: MutableLiveData<FirebaseUser>
        get() = userFirebase
    val getUser: MutableLiveData<User>
        get() = user
    val getLocation: MutableLiveData<ArrayList<Location>>
        get() = location
    val getLogStatus: MutableLiveData<Boolean>
        get() = logStatus

    val getLocationStatus: MutableLiveData<Boolean>
        get() = locationStatus
    init {
        accountRepository = AccountRepository(application)
        user = accountRepository.getUser
        userFirebase = accountRepository.getFirebaseUser
        logStatus = accountRepository.isCheckLog
        location = accountRepository.getUserLocation
        locationStatus = accountRepository.isCheckLocation
    }

    fun register(email: String, password: String, name: String){
        accountRepository.register(email, password, name)
    }

    fun login(email: String, password: String, isRemember: Boolean){
        accountRepository.login(email, password, isRemember)
    }
    fun getUserDetail(userId: String){
        accountRepository.getUserDetail(userId)
    }

    fun updateProfileUser(userId: String, user: User, imageUri: Uri){
        accountRepository.updateProfileUser(userId, user, imageUri)
    }
    fun logout(){
        accountRepository.logout()
    }
    fun addLocation(addressName: String, address: String, note: String, contactPersonName: String, contactPhoneNumber: String){
        accountRepository.addLocation(addressName, address, note, contactPersonName, contactPhoneNumber)
    }
    fun getLocation(){
        accountRepository.getLocation()
    }

    fun updateLocation(locationId: String, location: Location){
        accountRepository.updateLocation(locationId, location)
    }
    fun deleteLocation(locationId: String){
        accountRepository.deleteLocation(locationId)
    }
}