package com.example.foodapp.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.foodapp.model.User
import com.example.foodapp.until.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class LogRepository (_application: Application){
    private var firebaseUser: MutableLiveData<FirebaseUser>
    private var userLiveData: MutableLiveData<User>
    private var userLog: MutableLiveData<Boolean>

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

    val isCheckLog: MutableLiveData<Boolean>
        get() = userLog
    init {
        application = _application
        firebaseUser = MutableLiveData<FirebaseUser>()
        userLiveData = MutableLiveData<User>()
        userLog = MutableLiveData<Boolean>(false)

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
                val user = User(name, "", email, password, defaultImage)
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

    fun login(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                firebaseUser.postValue(auth.currentUser)
                userLog.postValue(true)
                Toast.makeText(application, "Đăng nhập thành công", Toast.LENGTH_LONG).show()
//                auth.currentUser.let {
//                    preferenceManager.putString("email", email)
//                    preferenceManager.putString("password", password)
//                }
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
    fun logout(){
        auth.signOut()
        preferenceManager.clear()
    }
}