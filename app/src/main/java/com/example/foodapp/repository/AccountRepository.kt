package com.example.foodapp.repository

import android.app.Application
import android.content.ContentValues
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.foodapp.model.Food
import com.example.foodapp.model.Location
import com.example.foodapp.model.User
import com.example.foodapp.until.PreferenceManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlin.random.Random

class AccountRepository (_application: Application){
    private var firebaseUser: MutableLiveData<FirebaseUser>
    private var userLiveData: MutableLiveData<User?>
    private var listUserData: MutableLiveData<ArrayList<User>>
    private var locationLiveData: MutableLiveData<ArrayList<Location>>
    private var userLog: MutableLiveData<Boolean>
    private var favouriteFoodLiveData: MutableLiveData<ArrayList<Food>>

    private var application: Application
    private var auth:FirebaseAuth
    private val defaultImage:String = "https://firebasestorage.googleapis.com/v0/b/food-app-2a650.appspot.com/o/user_image%2Fuser.png?alt=media&token=f220cf44-233f-43c2-9af7-fe907d00f938"
    private val storageReference: StorageReference
    private val preferenceManager: PreferenceManager
    private var database: FirebaseDatabase

    val getFirebaseUser: MutableLiveData<FirebaseUser>
        get() = firebaseUser

    val getUser: MutableLiveData<User?>
        get() = userLiveData
    val getUserLocation: MutableLiveData<ArrayList<Location>>
        get() = locationLiveData

    val isCheckLog: MutableLiveData<Boolean>
        get() = userLog

    val getFavouriteFood: MutableLiveData<ArrayList<Food>>
        get() = favouriteFoodLiveData
    val getListUserData: MutableLiveData<ArrayList<User>>
        get() = listUserData
    init {
        application = _application
        firebaseUser = MutableLiveData<FirebaseUser>()
        userLiveData = MutableLiveData<User?>()
        locationLiveData = MutableLiveData<ArrayList<Location>>()
        userLog = MutableLiveData<Boolean>(false)
        favouriteFoodLiveData = MutableLiveData<ArrayList<Food>>()
        listUserData = MutableLiveData<ArrayList<User>>()

        auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            firebaseUser.postValue(auth.currentUser)
        }
        storageReference =FirebaseStorage.getInstance().getReference("user_image/"+auth.currentUser?.uid)
        preferenceManager = PreferenceManager(application)
        database = Firebase.database
    }

    fun register(email: String, password: String, name: String, checkAdmin: Boolean){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { it ->
            if(it.isSuccessful){
                firebaseUser.postValue(auth.currentUser)
                userLog.postValue(true)
                val user = User(auth.currentUser?.uid.toString(), name, email, password, defaultImage, checkAdmin)
                database.getReference("Users").child(auth.currentUser?.uid.toString())
                    .setValue(user).addOnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(application, "Đăng ký thành công", Toast.LENGTH_LONG).show()
                            Log.d("Register", "Register Successful")
                        }
                    }
                    .addOnFailureListener {
                        Log.d("Register", it.message.toString())
                    }
            }else{
                userLog.postValue(false)
                Toast.makeText(application, "Đăng ký không thành công", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun login(email: String, password: String, isRemember: Boolean){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                firebaseUser.postValue(auth.currentUser)
                userLog.postValue(true)
                Toast.makeText(application, "Đăng nhập thành công", Toast.LENGTH_LONG).show()
                if(isRemember){
                    auth.currentUser.let {
                        preferenceManager.putBoolean("isRemember", true)
                        preferenceManager.putString("email", email)
                        preferenceManager.putString("password", password)
                    }
                }
            }else{
                userLog.postValue(false)
                Toast.makeText(application, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getListUser(){
        database.getReference("Users")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<User>()
                    for(itemSnap in snapshot.children){
                        itemSnap.getValue(User::class.java)?.let { list.add(it) }
                    }
                    listUserData.postValue(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("getListUser", "Failed to read value.", error.toException())
                }

            })
    }
    fun getFirebaseUser(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                firebaseUser.postValue(auth.currentUser)
            }
        }
    }

    fun getUserDetail(userID: String){
        database.getReference("Users").child(userID)
            .addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                userLiveData.postValue(user)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("getUserDetail", "Failed to read value.", error.toException())
            }
        })
    }

    fun getUserDetail(){
        database.getReference("Users").child(auth.currentUser?.uid.toString())
            .addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                userLiveData.postValue(user)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("getUserDetail", "Failed to read value.", error.toException())
            }

        })
    }

    fun updateImageUser(imageUri: Uri){
        storageReference.putFile(imageUri).addOnSuccessListener {
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                val updateUser:HashMap<String, Any> = HashMap()
                updateUser["imageUser"] = uri.toString()
                database.getReference("Users").child(auth.currentUser?.uid.toString())
                    .updateChildren(updateUser)
                    .addOnCompleteListener {
                        Log.d("update Image User", "Successful")
                    }
                    .addOnFailureListener {
                        Log.d("update Image User", "Fail")
                    }
            }
        }
    }

    fun updateProfileUser(user: User){
        val updateUser:HashMap<String, Any> = HashMap()
        updateUser["userName"] = user.userName.toString()
        updateUser["email"] = user.email.toString()
        database.getReference("Users").child(auth.currentUser?.uid.toString())
            .updateChildren(updateUser)
            .addOnCompleteListener {
                userLog.postValue(true)
                Toast.makeText(application, "cập nhật thành công", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                userLog.postValue(false)
                Toast.makeText(application, "cập nhật không thành công", Toast.LENGTH_LONG).show()
            }
    }
    fun updatePassword(userID: String, password: String){
        val update:HashMap<String, Any> = HashMap()
        update["password"] = password
        database.getReference("Users").child(userID)
            .updateChildren(update)
            .addOnCompleteListener {
                Toast.makeText(application, "Đổi mật khẩu thành công", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(application, "Đổi mật khẩu không thành công", Toast.LENGTH_LONG).show()
            }
    }

    fun updateInfoUserOrder(location: Location){
        val newLocation: HashMap<String, Any> = HashMap()
        newLocation["address"] = location.address.toString()
        newLocation["note"] = location.note.toString()
        newLocation["contactPersonName"] = location.contactPersonName.toString()
        newLocation["contactPhoneNumber"] = location.contactPhoneNumber.toString()
        database.getReference("Users").child(auth.currentUser?.uid.toString())
            .updateChildren(newLocation)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Location updated successfully!")
            }
            .addOnFailureListener {e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }


    fun addLocation(location: Location){
        val id = Random.nextInt(10000)
        val newLocation: HashMap<String, Any> = HashMap()
        newLocation["locationId"] = id.toString()
        newLocation["addressName"] = location.addressName.toString()
        newLocation["address"] = location.address.toString()
        newLocation["note"] = location.note.toString()
        newLocation["contactPersonName"] = location.contactPersonName.toString()
        newLocation["contactPhoneNumber"] = location.contactPhoneNumber.toString()
        newLocation["userId"] = location.userId.toString()
        database.getReference("Location").child(id.toString())
            .setValue(newLocation)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "addLocation Successful")
            }
            .addOnFailureListener {e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }

    fun getLocation(userID: String){
        database.getReference("Location").orderByChild("userId").equalTo(userID)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<Location>()
                    for(itemSnap in snapshot.children){
                        val location = itemSnap.getValue(Location::class.java)
                        list.add(location!!)
                    }
                    locationLiveData.postValue(list)
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read location.", error.toException())
                }

            })
    }

    fun updateLocation(locationId: String, location: Location){
        val newLocation: HashMap<String, Any> = HashMap()
        newLocation["addressName"] = location.addressName.toString()
        newLocation["address"] = location.address.toString()
        newLocation["note"] = location.note.toString()
        newLocation["contactPersonName"] = location.contactPersonName.toString()
        newLocation["contactPhoneNumber"] = location.contactPhoneNumber.toString()
        database.getReference("Location").child(locationId)
            .updateChildren(newLocation)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Location updated successfully!")
            }
            .addOnFailureListener {e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }

    fun deleteLocation(locationId: String){
        database.getReference("Location").child(locationId)
            .removeValue()
            .addOnCompleteListener {
                Log.d("Delete Location", "DocumentSnapshot successfully deleted!")
                Toast.makeText(application, "Xóa thành công", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
            }
    }

    fun logout(){
        auth.signOut()
        preferenceManager.clear()
    }

    fun addFavouriteFood(food: Food){
        val favourite = HashMap<String, Any>()
        favourite["foodId"] = food.foodId.toString()
        favourite["foodName"] = food.foodName.toString()
        favourite["price"] = food.price!!.toDouble()
        favourite["rating"] = food.rating!!.toDouble()
        favourite["time"] = food.time.toString()
        favourite["image"] = food.image.toString()
        favourite["describe"] = food.describe.toString()
        favourite["bestFood"] = food.bestFood!!
        favourite["adminId"] = food.adminId.toString()
        favourite["categoryId"] = food.categoryId.toString()
        favourite["userId"] = auth.currentUser?.uid.toString()
        database.getReference("FavouriteFood").child(food.foodId.toString())
            .setValue(favourite)
            .addOnSuccessListener {
                Toast.makeText(application, "Đã thêm món ăn vào mục yêu thích", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(application, "Thêm món ăn vào mục yêu thích thất bại", Toast.LENGTH_LONG).show()
            }
    }

    fun getFavouriteFood(){
        database.getReference("FavouriteFood").orderByChild("userId").equalTo(auth.currentUser?.uid.toString())
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = ArrayList<Food>()
                    for(itemSnap in snapshot.children){
                        val favourite = itemSnap.getValue(Food::class.java)
                        list.add(favourite!!)
                    }
                    favouriteFoodLiveData.postValue(list)
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read location.", error.toException())
                }

            })
    }

    fun deleteFavouriteFood(favouriteId: String){
        database.getReference("FavouriteFood").child(favouriteId)
            .removeValue()
            .addOnCompleteListener {
                Log.d("Delete FavouriteFood", "DocumentSnapshot successfully deleted!")
                Toast.makeText(application, "Xóa thành công", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(application, "Xóa không thành công", Toast.LENGTH_LONG).show()
            }
    }
}