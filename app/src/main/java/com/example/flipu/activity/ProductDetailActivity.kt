package com.example.flipu.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.flipu.MainActivity
import com.example.flipu.R
import com.example.flipu.databinding.ActivityProductDetailBinding
import com.example.flipu.roomdb.AppDatabase
import com.example.flipu.roomdb.ProductDao
import com.example.flipu.roomdb.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)

        getProductDetails(intent.getStringExtra("id"))

        setContentView(binding.root)
    }

    private fun getProductDetails(proId: String?) {

        Firebase.firestore.collection("products")
            .document(proId!!).get().addOnSuccessListener {
                val list = it.get("productImages") as ArrayList<String>
                val name = it.getString("productName")
                val productSp = it.getString("productSp")
                val productDesc = it.getString("productDescription")
                binding.textView.text = name
                binding.textView6.text = productSp
                binding.textView7.text = productDesc

                val slideList = ArrayList<SlideModel>()
                for (data in list){
                    slideList.add(SlideModel(data, ScaleTypes.CENTER_CROP))
                }

                cartAction(proId, name!!, productSp!!, it.getString("productCoverImg"))

                binding.imageSlider.setImageList(slideList)

            }.addOnFailureListener{
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }

    private fun cartAction(proId: String, name: String, productSp: String, coverImg: String?) {

        val productDao = AppDatabase.getInstance(this).productDao()
        if(productDao.isExist(proId) != null){
            binding.textView8.text = "Go to Cart"
        } else {
            binding.textView8.text = "Add to Cart"
        }

        binding.textView8.setOnClickListener{
            if(productDao.isExist(proId) != null){
                openCart()
            }else{
                addToCart(productDao, proId, name, productSp, coverImg)
            }
        }
    }

    private fun addToCart(productDao: ProductDao, proId: String, name: String, productSp: String, coverImg: String?) {
        val data = ProductModel(proId, name, coverImg, productSp)
        lifecycleScope.launch(Dispatchers.IO) {
            productDao.insertProduct(data)
            binding.textView8.text = "Go to Cart"
        }
    }

    private fun openCart() {
        val preference = this.getSharedPreferences("info", MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart", true)
        editor.apply()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}