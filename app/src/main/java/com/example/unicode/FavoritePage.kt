package com.example.unicode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unicode.databinding.ActivityFavoritePageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoritePage : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritePageBinding
//    var productsList = arrayListOf<ProductClass>()
    var productsList = arrayListOf<FavProduct>()

    lateinit var session: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFavoritePageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        session = SessionManager(applicationContext)

        binding.rcv.adapter = FavoriteAdapter(productsList,applicationContext)
        binding.rcv.layoutManager = LinearLayoutManager(applicationContext)
        val itemDecor = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        binding.rcv.addItemDecoration(itemDecor)


        binding.bottomNavigation.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.category -> Toast.makeText(applicationContext, "Category", Toast.LENGTH_LONG)
                    .show()
                R.id.home -> {
                    intent = Intent(applicationContext, AllProducts::class.java)
                    startActivity(intent)
                }
                R.id.account -> {
//                    intent.putStringArrayListExtra("userData",userData)
                    intent = Intent(applicationContext, AccountPage::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        val uId: String? = session.pref.getString(SessionManager.KEY_ID, null)
        allFavorite(uId.toString().toInt())
    }

    fun allFavorite(id: Int) {
        productsList.clear()
        val favClient = FavAPI.create()
//        val favClient = ProductAPI.create()
        favClient.favProduct(id)
//        favClient.productAll()
            .enqueue(object : Callback<List<FavProduct>> {
                override fun onResponse(
                    call: Call<List<FavProduct>>, response:
                    Response<List<FavProduct>>
                ) {
                    println(response.body())
                    response.body()?.forEach {
                        productsList.add(FavProduct(it.id,it.product_name,it.price,it.photo,))
//                        productsList.add(ProductClass(it.id, it.product_name,it.price,it.detail,it.photo,it.amount,it.subtype_id))
                    }
//                    binding.rcv.adapter = ProductsAdapter(productsList, applicationContext)
                    binding.rcv.adapter = FavoriteAdapter(productsList, applicationContext)
                }

                override fun onFailure(call: Call<List<FavProduct>>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Error onFailure " + t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}