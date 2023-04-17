package com.example.flipu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.flipu.databinding.AllOrdersItemLayoutBinding
import com.example.flipu.model.AllOrderModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AllOrderAdapter (val list: ArrayList<AllOrderModel>, val context : Context)
    : RecyclerView.Adapter<AllOrderAdapter.AllOrdersViewHolder>(){

    inner class AllOrdersViewHolder (val binding: AllOrdersItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllOrdersViewHolder {
        return AllOrdersViewHolder(
            AllOrdersItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AllOrdersViewHolder, position: Int) {
        holder.binding.productTitle.text = list[position].name
        holder.binding.productPrice.text = list[position].price

        when(list[position].status){
            "Ordered" -> {
                holder.binding.productStatus.text = "Ordered"
            }
            "Dispatched" -> {
                holder.binding.productStatus.text = "Dispatched"
            }
            "Delivered" -> {
                holder.binding.productStatus.text = "Delivered"
            }
            "Canceled" -> {
                holder.binding.productStatus.text = "Canceled"
            }
        }
    }
}