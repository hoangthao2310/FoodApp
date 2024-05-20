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
    private var user: MutableLiveData<User?>
    private var location: MutableLiveData<ArrayList<Location>>
    private var userFirebase: MutableLiveData<FirebaseUser>
    private var logStatus: MutableLiveData<Boolean>
    private var favouriteFood: MutableLiveData<ArrayList<Food>>
    private var listUser: MutableLiveData<ArrayList<User>>

    val getUserData: MutableLiveData<FirebaseUser>
        get() = userFirebase
    val getUser: MutableLiveData<User?>
        get() = user
    val getLocation: MutableLiveData<ArrayList<Location>>
        get() = location
    val getLogStatus: MutableLiveData<Boolean>
        get() = logStatus
    val getFavouriteFood: MutableLiveData<ArrayList<Food>>
        get() = favouriteFood
    val getListUser: MutableLiveData<ArrayList<User>>
        get() = listUser
    init {
        accountRepository = AccountRepository(application)
        user = accountRepository.getUser
        userFirebase = accountRepository.getFirebaseUser
        logStatus = accountRepository.isCheckLog
        location = accountRepository.getUserLocation
        favouriteFood = accountRepository.getFavouriteFood
        listUser = accountRepository.getListUserData
    }

    fun register(email: String, password: String, name: String, checkAdmin: Boolean){
        accountRepository.register(email, password, name, checkAdmin)
    }

    fun login(email: String, password: String, isRemember: Boolean){
        accountRepository.login(email, password, isRemember)
    }
    fun getListUser(){
        accountRepository.getListUser()
    }
    fun getFirebaseUser(email: String, password: String){
        accountRepository.getFirebaseUser(email, password)
    }
    fun getUserDetail(userId: String){
        accountRepository.getUserDetail(userId)
    }
    fun getUserDetail(){
        accountRepository.getUserDetail()
    }
    fun updateImageUser(imageUri: Uri){
        accountRepository.updateImageUser(imageUri)
    }
    fun updateProfileUser(user: User){
        accountRepository.updateProfileUser(user)
    }
    fun updatePassword(userId: String, password: String){
        accountRepository.updatePassword(userId, password)
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
    fun getLocation(userId: String){
        accountRepository.getLocation(userId)
    }
    fun updateLocation(locationId: String, location: Location){
        accountRepository.updateLocation(locationId, location)
    }
    fun deleteLocation(locationId: String){
        accountRepository.deleteLocation(locationId)
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
}