package com.example.unicode

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.unicode.databinding.ActivityProductPageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductPage : AppCompatActivity() {
    private lateinit var binding: ActivityProductPageBinding
    val productClient = ProductAPI.create()
    val favClient = FavAPI.create()
    var sizeList = arrayListOf<SizeClass>()
    var pId: String = ""
    lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        session = SessionManager(applicationContext)
        binding = ActivityProductPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pId = intent.getStringExtra("product_id").toString()
        val pName = intent.getStringExtra("product_name").toString()
        val pPrice = intent.getStringExtra("product_price").toString()
        val pPhoto = intent.getStringExtra("product_photo").toString()
        val pDetail = intent.getStringExtra("product_detail")
        val uId: String? = session.pref.getString(SessionManager.KEY_ID, null)

        binding.productName.text = pName
        binding.productPrice.text = pPrice + " THB."
        binding.detail.text = pDetail
        Glide.with(this).load(pPhoto).into(binding.productImg)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rcvSize.adapter = SizeAdapter(this.sizeList, applicationContext)
        binding.rcvSize.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            true
        )

        binding.productNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.fav -> {
                    var intent = Intent(applicationContext, FavoritePage::class.java)
                    startActivity(intent)
                }
                R.id.basket -> {
                    Toast.makeText(applicationContext, "Basket", Toast.LENGTH_LONG).show()
                }
            }
            true
        }

        binding.btnFav.setOnClickListener {
            favClient.addFav(uId.toString().toInt(),pId.toInt())
                .enqueue(object : Callback<FavProduct> {
                    override fun onResponse(call: Call<FavProduct>, response: Response<FavProduct>) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext,"Add to favorite product", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(applicationContext,"Add failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<FavProduct>, t: Throwable) {
                        println(t.message)
                        Toast.makeText(
                            applicationContext,
                            "Error onFailure" + t.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }
    }

    override fun onResume() {
        super.onResume()
        pId = intent.getStringExtra("product_id").toString()
        callSize(pId.toInt())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun callSize(pId: Int) {
        sizeList.clear()
        val productClient = ProductAPI.create()
        productClient.findProductSize(pId)
            .enqueue(object : Callback<List<SizeClass>> {
                override fun onResponse(
                    call: Call<List<SizeClass>>, response:
                    Response<List<SizeClass>>
                ) {
                    println(response.body())
                    response.body()?.reversed()?.forEach {
                        sizeList.add(
                            SizeClass(
                                it.id, it.size
                            )
                        )
                    }
                    binding.rcvSize.adapter = SizeAdapter(sizeList, applicationContext)
                }

                override fun onFailure(call: Call<List<SizeClass>>, t: Throwable) {
                    println(t.message)
                    Toast.makeText(
                        applicationContext,
                        "Error onFailure " + t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}