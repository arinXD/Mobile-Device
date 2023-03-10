package com.example.unicode

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.unicode.databinding.FavoriteItemBinding

class FavoriteAdapter (val items : ArrayList<FavProduct>, val context: Context) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, val binding: FavoriteItemBinding):
        RecyclerView.ViewHolder(view){
        init{
            binding.btnAddtoCart.setOnClickListener {
//                val item = items[adapterPosition]
//                val contextView : Context = view.context
//                val intent = Intent(context, ProductPage::class.java)
//                intent.putExtra("product_id",item.id.toString())
//                intent.putExtra("product_detail",item.detail)
//                intent.putExtra("product_name",item.product_name)
//                intent.putExtra("product_price",item.price.toString())
//                intent.putExtra("product_photo",item.photo)
//                contextView.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdapter.ViewHolder {
        val binding = FavoriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.ViewHolder, position: Int) {
        val binding_holder = holder.binding

        binding_holder.favProductName?.text = items[position].product_name
        binding_holder.favProductPrice?.text = String.format("%,d", items[position].price) + " THB."
        Glide.with(context).load(items[position].photo).into(binding_holder.favProductImg)
    }

    override fun getItemCount(): Int {
        return items.size
    }

}