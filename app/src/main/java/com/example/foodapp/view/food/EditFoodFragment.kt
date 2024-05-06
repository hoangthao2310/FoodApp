package com.example.foodapp.view.food

import android.R
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
import com.example.foodapp.databinding.FragmentEditFoodBinding
import com.example.foodapp.model.Food
import com.example.foodapp.viewmodel.FoodViewModel

class EditFoodFragment : BaseFragment<FragmentEditFoodBinding>() {
    private lateinit var foodViewModel: FoodViewModel
    private lateinit var food: Food
    private var imageUri: Uri? = null
    private var categoryName: String ? = null
    private var bestFood: Boolean = false
    private var categoryId: String ? = null

    companion object{
        private const val REQUEST_CODE_PICK_IMAGE = 100
    }
    override fun getLayout(container: ViewGroup?): FragmentEditFoodBinding =
        FragmentEditFoodBinding.inflate(layoutInflater, container, false)

    override fun initViews() {
        foodViewModel = ViewModelProvider(this)[FoodViewModel::class.java]
        food = data as Food

        foodViewModel.foodDetailAdmin(food.foodId.toString())
        foodViewModel.getFoodDetail.observe(viewLifecycleOwner){
            binding.edtFoodName.setText(it.foodName)
            binding.edtPrice.setText(it.price.toString())
            binding.edtDescribe.setText(it.describe)
            binding.edtTime.setText(it.time)
            if(it.bestFood == true){
                binding.cbBestFood.isChecked = true
            }
            foodViewModel.getCategoryName(it.categoryId.toString())
            foodViewModel.getCateDetail.observe(viewLifecycleOwner){name ->
                categoryName = name
            }
            Glide.with(requireContext()).load(it.image).into(binding.imgFood)
        }

        foodViewModel.categoryName()
        foodViewModel.getCategoryDetail.observe(viewLifecycleOwner){ listCategoryName ->
            val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, listCategoryName)
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

        binding.btnEditFood.setOnClickListener {
            if(binding.cbBestFood.isChecked){
                bestFood = true
            }

            foodViewModel.categoryId(categoryName.toString())
            foodViewModel.getCateDetail.observe(viewLifecycleOwner){id ->
                categoryId = id
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
            foodViewModel.updateFoodAdmin(newFood, food.foodId)
            foodViewModel.isCheck.observe(viewLifecycleOwner){check ->
                if(check){
                    loading(false)
                    parentFragmentManager.popBackStack()
                    notify("Chỉnh sửa thành công")
                }
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
            binding.btnEditFood.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.btnEditFood.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

}