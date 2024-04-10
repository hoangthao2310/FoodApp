package com.example.foodapp.view.profile

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentEditProfileBinding
import com.example.foodapp.model.User
import com.example.foodapp.viewmodel.LogViewModel
import com.google.firebase.auth.FirebaseUser

class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>() {
    private lateinit var logViewModel: LogViewModel
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var imageUri: Uri

    companion object{
        private const val REQUEST_CODE_PICK_IMAGE = 100
    }
    override fun getLayout(container: ViewGroup?): FragmentEditProfileBinding =
        FragmentEditProfileBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        logViewModel = ViewModelProvider(this)[LogViewModel::class.java]
        firebaseUser = data as FirebaseUser
        logViewModel.getUserDetail(firebaseUser.uid)

        logViewModel.getUser.observe(viewLifecycleOwner){user ->
            binding.edtUserName.setText(user.userName)
            binding.edtEmail.setText(user.emailAdress)
            Glide.with(requireActivity()).load(user.imageUser).into(binding.imgUser)
        }

        binding.btnSaveChanges.setOnClickListener {
            val userNew = User(
                userName = binding.edtUserName.text.toString(),
                emailAdress = binding.edtEmail.text.toString(),
                phoneNumber = binding.edtPhoneNumber.text.toString()
            )

            loading(true)
            logViewModel.updateProfileUser(firebaseUser.uid, userNew, imageUri)
            logViewModel.getUserData.observe(viewLifecycleOwner){userData ->
                logViewModel.getLogStatus.observe(this){
                    if(it){
                        loading(false)
                        callback.showFragment(EditProfileFragment::class.java, ProfileFragment::class.java, 0,0, userData)
                    }
                }
            }

        }

        binding.btnPickImage.setOnClickListener {
            pickImageGallery()
        }

        binding.btnBack.setOnClickListener {
            callback.backToPrevious()
        }
    }


    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == AppCompatActivity.RESULT_OK){
            imageUri = data?.data!!
            Glide.with(this).load(imageUri).into(binding.imgUser)
        }
    }

    private fun loading(isLoading: Boolean){
        if(isLoading){
            binding.btnSaveChanges.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.btnSaveChanges.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

}