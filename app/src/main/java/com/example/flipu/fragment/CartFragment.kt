package com.example.flipu.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flipu.R
import com.example.flipu.activity.AddressActivity
import com.example.flipu.activity.CategoryActivity
import com.example.flipu.adapter.CartAdapter
import com.example.flipu.databinding.FragmentCartBinding
import com.example.flipu.roomdb.AppDatabase
import com.example.flipu.roomdb.ProductModel

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding

    private lateinit var list: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(layoutInflater)

        val preference = requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart", false)
        editor.apply()

        val dao = AppDatabase.getInstance(requireContext()).productDao()

        list = ArrayList()

        dao.getAllProducts().observe(requireActivity()){
            binding.cartRecycler.adapter = CartAdapter(requireContext(), it)

            list.clear()
            for(data in it){
                list.add(data.productId)
            }

            totalCost(it)
        }

        return binding.root
    }

    private fun totalCost(data: List<ProductModel>?) {
        var total = 0
        for(item in data!!){
            total += item.productSp!!.toInt()
        }
        binding.textView12.text = "Total item in cart is ${data.size}"
        binding.textView13.text = "Total Cost : $total"

        binding.checkout.setOnClickListener{
            if(list.isEmpty()){
                Toast.makeText(context, "Please add product in cart to checkout", Toast.LENGTH_SHORT).show()

            }
            else {
                val intent = Intent(context, AddressActivity::class.java)

                val b = Bundle()
                b.putStringArrayList("productIds", list)
                b.putString("totalCost", total.toString())
                intent.putExtras(b)
                startActivity(intent)
            }
        }

    }

}