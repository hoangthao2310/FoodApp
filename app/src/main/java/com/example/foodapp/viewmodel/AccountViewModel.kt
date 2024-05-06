package com.example.foodapp.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.foodapp.model.Food
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
    private var logAdminStatus: MutableLiveData<Boolean>
    private var favouriteFood: MutableLiveData<ArrayList<Food>>
    private var infoAdmin: MutableLiveData<String>

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
    val getLogAdminStatus: MutableLiveData<Boolean>
        get() = logAdminStatus
    val getFavouriteFood: MutableLiveData<ArrayList<Food>>
        get() = favouriteFood
    val getInfoAdmin: MutableLiveData<String>
        get() = infoAdmin
    init {
        accountRepository = AccountRepository(application)
        user = accountRepository.getUser
        userFirebase = accountRepository.getFirebaseUser
        logStatus = accountRepository.isCheckLog
        location = accountRepository.getUserLocation
        locationStatus = accountRepository.isCheckLocation
        logAdminStatus = accountRepository.getCheckAdmin
        favouriteFood = accountRepository.getFavouriteFood
        infoAdmin = accountRepository.getInfoAdmin
    }

    fun register(email: String, password: String, name: String, checkAdmin: Boolean){
        accountRepository.register(email, password, name, checkAdmin)
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
    fun updateInfoUserOrder(location: Location){
        accountRepository.updateInfoUserOrder(location)
    }
    fun logout(){
        accountRepository.logout()
    }
    fun addLocation(location: Location){
        accountRepository.addLocation(location)
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
    fun checkAdmin(){
        accountRepository.checkAdmin()
    }
    fun addFavouriteFood(food: Food){
        accountRepository.addFavouriteFood(food)
    }
    fun getFavouriteFood(){
        accountRepository.getFavouriteFood()
    }
    fun deleteFavouriteFood(foodId: String){
        accountRepository.deleteFavouriteFood(foodId)
    }
    fun getInfoAdmin(adminId: String){
        accountRepository.getInfoAdmin(adminId)
    }
}