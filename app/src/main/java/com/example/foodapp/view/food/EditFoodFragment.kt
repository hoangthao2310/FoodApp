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
import com.example.foodapp.databinding.FragmentEditFoodBinding
import com.example.foodapp.model.Food
import com.example.foodapp.viewmodel.FoodViewModel

class EditFoodFragment : BaseFragment<FragmentEditFoodBinding>() {
    private lateinit var foodViewModel: FoodViewModel
    private lateinit var food: Food
    private var imageUri: Uri? = null
    private var bestFood: Boolean = false
    private var categoryName: String? = null
    private var categoryId: String? = null
    private lateinit var listCategoryName: ArrayList<String>
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>

    override fun getLayout(container: ViewGroup?): FragmentEditFoodBinding =
        FragmentEditFoodBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        foodViewModel = ViewModelProvider(this)[FoodViewModel::class.java]
        food = data as Food

        listCategoryName = ArrayList()

        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                imageUri = result.data?.data
                imageUri?.let {
                    Glide.with(this).load(it).into(binding.imgFood)
                }
            }
        }

        binding.edtFoodName.setText(food.foodName)
        binding.edtPrice.setText(food.price.toString())
        binding.edtDescribe.setText(food.describe)
        binding.edtTime.setText(food.time)
        if(food.bestFood == true){
            binding.cbBestFood.isChecked = true
        }
        categoryId = food.categoryId
        Glide.with(requireContext()).load(food.image).into(binding.imgFood)

        foodViewModel.category()
        foodViewModel.getCategory.observe(viewLifecycleOwner) { listCategory ->
            listCategoryName.clear()
            listCategory.forEach { category ->
                if(categoryId == category.categoryId){
                    categoryName = category.categoryName
                }
                listCategoryName.add(category.categoryName.toString())
            }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listCategoryName)
            binding.spinnerCategory.adapter = adapter
            binding.spinnerCategory.setSelection(listCategoryName.indexOf(categoryName))
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

        binding.btnEditFood.setOnClickListener {
            if(binding.cbBestFood.isChecked){
                bestFood = true
            }

            foodViewModel.category()
            foodViewModel.getCategory.observe(viewLifecycleOwner) { listCategory ->
                listCategory.forEach { category ->
                    if (category.categoryName == categoryName) {
                        categoryId = category.categoryId
                    }
                }
            }

            val newFood = Food(
                foodName = binding.edtFoodName.text.toString(),
                price = binding.edtPrice.text.toString().toDouble(),
                describe = binding.edtDescribe.text.toString(),
                time = binding.edtTime.text.toString(),
                bestFood = bestFood,
                categoryId = categoryId,
            )

            loading(true)

            if(imageUri != null){
                foodViewModel.updateImage(food.foodId.toString(), imageUri!!)
            }
            foodViewModel.updateFoodAdmin(newFood, food.foodId.toString())
            loading(false)
            parentFragmentManager.popBackStack()
            notify("Chỉnh sửa thành công")
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
        binding.btnEditFood.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

}