package com.example.unicode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unicode.databinding.ActivityCompleteProductPageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompleteProductPage : AppCompatActivity() {
    private lateinit var binding: ActivityCompleteProductPageBinding
    public var priceList = arrayListOf<OrderDetailShopBagProduct>()
    lateinit var session: SessionManager
    var orderId: Int = 0
    val orderApi = OrderAPI.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        session = SessionManager(applicationContext)
        binding = ActivityCompleteProductPageBinding.inflate(layoutInflater)
        val uId: String? = session.pref.getString(SessionManager.KEY_ID, null)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var priceAll = intent.getIntExtra("priceAll", 0)
        binding.priceAll.text = "ยอดรวม " + String.format("%,d", priceAll) + " บาท"

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.recyclerViewProductPrice.adapter =
            OrderDetailShoppingBagAdapter(priceList, applicationContext)
        binding.recyclerViewProductPrice.layoutManager = LinearLayoutManager(applicationContext)
        val itemDecor = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        binding.recyclerViewProductPrice.addItemDecoration(itemDecor)

        orderApi.retrieveOrder(uId.toString().toInt())
            .enqueue(object : Callback<Order> {
                override fun onResponse(call: Call<Order>, response: Response<Order>) {
                    if (response.isSuccessful) {
//                        Toast.makeText(applicationContext,"can find order", Toast.LENGTH_SHORT).show()
                        orderId = response.body()?.id!!.toInt()
                        binding.createdAt.text = response.body()?.created_at.toString()
                        println("orderId 0: " + orderId)
                    } else {
                        Toast.makeText(applicationContext, "cant find order id", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<Order>, t: Throwable) {
                    println(t.message)
                    Toast.makeText(
                        applicationContext,
                        "error on failed" + t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        println("orderId -1: " + orderId)
    }

    override fun onResume() {
        super.onResume()
        val uId: String? = session.pref.getString(SessionManager.KEY_ID, null)
        callProduct(uId.toString().toInt())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun callProduct(id: Int) {
        priceList.clear()
        val orderApi = OrderAPI.create()
        orderApi.callProduct(id)
            .enqueue(object : Callback<List<OrderProductClass>> {
                override fun onResponse(
                    call: Call<List<OrderProductClass>>, response:
                    Response<List<OrderProductClass>>
                ) {
                    println(response.body())
                    response.body()?.forEach {
                        priceList.add(
                            OrderDetailShopBagProduct(
                                it.product_name,
                                it.amount,
                                it.product_price,
                                it.price_all
                            )
                        )

                    }
                    binding.recyclerViewProductPrice.adapter =
                        OrderDetailShoppingBagAdapter(priceList, applicationContext)
//                    binding.priceAll.text = priceAll.toString() +" บาท"
                }

                override fun onFailure(call: Call<List<OrderProductClass>>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Error onFailure " + t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}