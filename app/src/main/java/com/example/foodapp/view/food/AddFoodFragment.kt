package com.example.foodapp.view.food

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodapp.base.BaseFragment
import com.example.foodapp.databinding.FragmentAddFoodBinding
import com.example.foodapp.model.Food
import com.example.foodapp.view.home.HomeAdminFragment
import com.example.foodapp.viewmodel.FoodViewModel
import com.google.firebase.auth.FirebaseUser

class AddFoodFragment : BaseFragment<FragmentAddFoodBinding>() {
    private lateinit var foodViewModel: FoodViewModel
    private var imageUri: Uri? = null
    private lateinit var firebaseUser: FirebaseUser

    private var bestFood: Boolean = false
    private var categoryName: String? =null

    companion object{
        private const val REQUEST_CODE_PICK_IMAGE = 100
    }
    override fun getLayout(container: ViewGroup?): FragmentAddFoodBinding =
        FragmentAddFoodBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        foodViewModel = ViewModelProvider(this)[FoodViewModel::class.java]
        firebaseUser = data as FirebaseUser

        foodViewModel.categoryName()
        foodViewModel.getCategoryDetail.observe(viewLifecycleOwner){ listCategoryName ->
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listCategoryName)
            binding.spinnerCategory.adapter = adapter
            binding.spinnerCategory.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    categoryName = listCategoryName[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }


        binding.btnSelectImage.setOnClickListener {
            pickImageGallery()
        }

        if(binding.cbBestFood.isChecked){
            bestFood = true
        }

        binding.btnAddNewFood.setOnClickListener {
            if(categoryName != null){
                foodViewModel.categoryId(categoryName.toString())
                foodViewModel.getCateDetail.observe(viewLifecycleOwner){categoryId ->
                    val food = Food(
                        foodName = binding.edtFoodName.text.toString(),
                        price = binding.edtPrice.text.toString().toDouble(),
                        rating = 5.0,
                        time = binding.edtTime.text.toString(),
                        image = imageUri.toString(),
                        describe = binding.edtDescribe.text.toString(),
                        bestFood = bestFood,
                        adminId = firebaseUser.uid,
                        categoryId = categoryId,
                        favouriteId = ""
                    )
                    loading(true)
                    if(imageUri != null){
                        foodViewModel.addFood(food, imageUri!!)
                        foodViewModel.isCheck.observe(viewLifecycleOwner){
                            if(it){
                                loading(false)
                                callback.showFragment(AddFoodFragment::class.java, HomeAdminFragment::class.java, 0, 0, data, false)
                                notify("Thêm món ăn thành công")
                            }
                        }
                    }else{
                        notify("Vui lòng chọn ảnh!!!")
                    }

                }
            }else{
                notify("Vui lòng chọn danh mục cho món ăn")
            }
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
            Glide.with(this).load(imageUri).into(binding.imgFood)
        }
    }

    private fun loading(isLoading: Boolean){
        if(isLoading){
            binding.btnAddNewFood.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.btnAddNewFood.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
        }
    }
}