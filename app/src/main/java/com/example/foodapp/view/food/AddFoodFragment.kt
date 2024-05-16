package com.example.foodapp.view.food

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
    private var categoryName: String? = null
    private var categoryId: String? = null
    private lateinit var listCategoryName: ArrayList<String>
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>

    override fun getLayout(container: ViewGroup?): FragmentAddFoodBinding =
        FragmentAddFoodBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        foodViewModel = ViewModelProvider(this)[FoodViewModel::class.java]
        firebaseUser = data as FirebaseUser
        listCategoryName = ArrayList()

        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                imageUri = result.data?.data
                imageUri?.let {
                    Glide.with(this).load(it).into(binding.imgFood)
                }
            }
        }

        foodViewModel.category()
        foodViewModel.getCategory.observe(viewLifecycleOwner) { listCategory ->
            listCategoryName.clear()
            listCategory.forEach { category ->
                listCategoryName.add(category.categoryName.toString())
            }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listCategoryName)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCategory.adapter = adapter
            binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    categoryName = listCategoryName[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

        binding.btnSelectImage.setOnClickListener {
            pickImageGallery()
        }

        binding.btnAddNewFood.setOnClickListener {
            if (categoryName != null) {
                bestFood = binding.cbBestFood.isChecked
                foodViewModel.category()
                foodViewModel.getCategory.observe(viewLifecycleOwner) { listCategory ->
                    listCategory.forEach { category ->
                        if(category.categoryName == categoryName){
                            categoryId = category.categoryId
                        }
                    }
                }
                val food = Food(
                    foodName = binding.edtFoodName.text.toString(),
                    price = binding.edtPrice.text.toString().toDouble(),
                    rating = 5.0,
                    time = binding.edtTime.text.toString(),
                    image = imageUri.toString(),
                    describe = binding.edtDescribe.text.toString(),
                    bestFood = bestFood,
                    adminId = firebaseUser.uid,
                    categoryId = categoryId
                )
                loading(true)
                if (imageUri != null) {
                    foodViewModel.addFood(food, imageUri!!)
                    callback.showFragment(AddFoodFragment::class.java, HomeAdminFragment::class.java, 0, 0, data, true)
                    notify("Thêm món ăn thành công")
                } else {
                    loading(false)
                    notify("Vui lòng chọn ảnh!!!")
                }
            } else {
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
        pickImageLauncher.launch(intent)
    }

    private fun loading(isLoading: Boolean) {
        binding.btnAddNewFood.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }
}
