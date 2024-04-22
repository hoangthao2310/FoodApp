package com.example.foodapp.repository

import android.app.Application
import android.content.ContentValues
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.foodapp.model.Location
import com.example.foodapp.model.User
import com.example.foodapp.until.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AccountRepository (_application: Application){
    private var firebaseUser: MutableLiveData<FirebaseUser>
    private var userLiveData: MutableLiveData<User>
    private var locationLiveData: MutableLiveData<ArrayList<Location>>
    private var userLog: MutableLiveData<Boolean>
    private var checkLocation: MutableLiveData<Boolean>

    private var application: Application
    private var auth:FirebaseAuth
    private val defaultImage:String = "https://firebasestorage.googleapis.com/v0/b/food-app-2a650.appspot.com/o/user_image%2Fuser.png?alt=media&token=f220cf44-233f-43c2-9af7-fe907d00f938"
    private val firestoreFirebase: FirebaseFirestore
    private val storageReference: StorageReference
    private val preferenceManager: PreferenceManager

    val getFirebaseUser: MutableLiveData<FirebaseUser>
        get() = firebaseUser

    val getUser: MutableLiveData<User>
        get() = userLiveData
    val getUserLocation: MutableLiveData<ArrayList<Location>>
        get() = locationLiveData

    val isCheckLog: MutableLiveData<Boolean>
        get() = userLog

    val isCheckLocation: MutableLiveData<Boolean>
        get() = checkLocation
    init {
        application = _application
        firebaseUser = MutableLiveData<FirebaseUser>()
        userLiveData = MutableLiveData<User>()
        locationLiveData = MutableLiveData<ArrayList<Location>>()
        userLog = MutableLiveData<Boolean>(false)
        checkLocation = MutableLiveData<Boolean>(false)

        auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            firebaseUser.postValue(auth.currentUser)
        }
        firestoreFirebase = FirebaseFirestore.getInstance()
        storageReference =FirebaseStorage.getInstance().getReference("user_image/"+auth.currentUser?.uid)
        preferenceManager = PreferenceManager(application)
    }

    fun register(email: String, password: String, name: String){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { it ->
            if(it.isSuccessful){
                firebaseUser.postValue(auth.currentUser)
                userLog.postValue(true)
                val user = User(name, email, password, defaultImage)
                firestoreFirebase.collection("users").document(auth.uid!!)
                    .set(user)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(application, "Cập nhật thông tin người dùng thành công", Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(application, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                Toast.makeText(application, "Đăng ký thành công", Toast.LENGTH_LONG).show()
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
    fun getUserDetail(userID: String){
        firestoreFirebase.collection("users").document(userID)
            .get()
            .addOnSuccessListener {documentSnapshot ->
                if(documentSnapshot.exists()){
                    val user = documentSnapshot.toObject(User::class.java)
                    userLiveData.postValue(user!!)
                }
            }
            .addOnFailureListener {
                Log.d("getUserDetail", "Error getting user detail: $it")
            }
    }

    fun updateProfileUser(userId: String, user: User, imageUri: Uri){
        storageReference.putFile(imageUri).addOnSuccessListener {
            storageReference.downloadUrl.addOnSuccessListener { uri ->
                val updateUser:HashMap<String, Any> = HashMap()
                updateUser["userName"] = user.userName.toString()
                updateUser["emailAdress"] = user.emailAdress.toString()
                updateUser["imageUser"] = uri.toString()
                firestoreFirebase.collection("users").document(userId)
                    .update(updateUser)
                    .addOnCompleteListener {
                        userLog.postValue(true)
                        Toast.makeText(application, "cập nhật thành công", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener {
                        userLog.postValue(false)
                        Toast.makeText(application, "cập nhật không thành công", Toast.LENGTH_LONG).show()
                    }
            }
        }
    }

    fun addLocation(addressName: String, address: String, note: String, contactPersonName: String, contactPhoneNumber: String){
        val newLocation: HashMap<String, Any> = HashMap()
        newLocation["addressName"] = addressName
        newLocation["address"] = address
        newLocation["note"] = note
        newLocation["contactPersonName"] = contactPersonName
        newLocation["contactPhoneNumber"] = contactPhoneNumber
        firestoreFirebase.collection("users").document(auth.currentUser!!.uid).collection("location")
            .add(newLocation)
            .addOnSuccessListener { documentReference ->
                checkLocation.postValue(true)
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener {e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }

    fun getLocation(){
        firestoreFirebase.collection("users").document(auth.currentUser!!.uid).collection("location")
            .get()
            .addOnCompleteListener {task ->
                if(task.isSuccessful && task.result!=null){
                    val listLocation = ArrayList<Location>()
                    for(document in task.result){
                        val locationId = document.id
                        val addressName = document.getString("addressName")
                        val address = document.getString("address")
                        val note = document.getString("note")
                        val contactPersonName = document.getString("contactPersonName")
                        val contactPhoneNumber = document.getString("contactPhoneNumber")
                        val location = Location(locationId, addressName, address, note, contactPersonName, contactPhoneNumber)
                        listLocation.add(location)
                    }
                    locationLiveData.postValue(listLocation)
                }
            }
            .addOnFailureListener {
                Log.d("getLocation", "Error getting location: $it")
            }
    }

    fun updateLocation(locationId: String, location: Location){
        val newLocation: HashMap<String, Any> = HashMap()
        newLocation["addressName"] = location.addressName.toString()
        newLocation["address"] = location.address.toString()
        newLocation["note"] = location.note.toString()
        newLocation["contactPersonName"] = location.contactPersonName.toString()
        newLocation["contactPhoneNumber"] = location.contactPhoneNumber.toString()
        firestoreFirebase.collection("users").document(auth.currentUser!!.uid)
            .collection("location").document(locationId)
            .update(newLocation)
            .addOnSuccessListener {
                checkLocation.postValue(true)
                Log.d(ContentValues.TAG, "Location updated successfully!")
            }
            .addOnFailureListener {e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
    }

    fun deleteLocation(locationId: String){
        firestoreFirebase.collection("users").document(auth.currentUser!!.uid).collection("location").document(locationId)
            .delete()
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
}