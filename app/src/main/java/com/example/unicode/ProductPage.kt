package com.example.unicode

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
    var sizeList = arrayListOf<SizeClass>()
    var pId : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        pId = intent.getStringExtra("product_id").toString()
//        callSize(pId.toInt())
        val pName = intent.getStringExtra("product_name").toString()
        val pPrice = intent.getStringExtra("product_price").toString()
        val pPhoto = intent.getStringExtra("product_photo").toString()
        val pDetail = intent.getStringExtra("product_detail")
        println("--------------------------\n${pDetail}")

        binding.productName.text = pName
        binding.productPrice.text = pPrice+" THB."
        binding.detail.text = pDetail
        Glide.with(this).load(pPhoto).into(binding.productImg)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rcvSize.adapter = SizeAdapter(this.sizeList, applicationContext)
//        binding.rcvSize.layoutManager = LinearLayoutManager(applicationContext)
//        binding.rcvSize.addItemDecoration(
//            DividerItemDecoration(binding.rcvSize.context,
//            DividerItemDecoration.HORIZONTAL, true)
//        )
        binding.rcvSize.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            true
        )
    }

    override fun onResume() {
        super.onResume()
        pId = intent.getStringExtra("product_id").toString()
        callSize(pId.toInt())
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId== android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    fun callSize(pId: Int){
        sizeList.clear()
        val productClient = ProductAPI.create()
        productClient.findProductSize(pId)
            .enqueue(object : Callback<List<SizeClass>> {
                override fun onResponse(call: Call<List<SizeClass>>, response:
                Response<List<SizeClass>>) {
                    println(response.body())
                    response.body()?.reversed()?.forEach {
                        sizeList.add(SizeClass(
                            it.id, it.size)) }
                    binding.rcvSize.adapter = SizeAdapter(sizeList, applicationContext)
                }
                override fun onFailure(call: Call<List<SizeClass>>, t: Throwable) {
                    println(t.message)
                    Toast.makeText(applicationContext,"Error onFailure " + t.message, Toast.LENGTH_LONG).show()
                }
            })
    }
}