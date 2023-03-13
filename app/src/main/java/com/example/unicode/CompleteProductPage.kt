package com.example.unicode

import android.content.Intent
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
    var priceList = arrayListOf<OrderDetailShopBagProduct>()
    lateinit var session: SessionManager
    var orderId: Int = 0
    var addressID: Int = 0
    var id = 0
    private val orderApi = OrderAPI.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        session = SessionManager(applicationContext)
        val uId: String? = session.pref.getString(SessionManager.KEY_ID, null)
        id = uId.toString().toInt()
        binding = ActivityCompleteProductPageBinding.inflate(layoutInflater)
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

        println("orderId -1: $orderId")
        println("address ID: $addressID")

//        binding.editAddressProductComplete.text = "ที่อยู่ $addressID"

        binding.editAddressProductComplete.setOnClickListener {
            startActivity(Intent(applicationContext, SelectAddress::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        callOrder(id)
        val uId: String? = session.pref.getString(SessionManager.KEY_ID, null)
        callProduct(uId.toString().toInt())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun callProduct(id: Int) {
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
                        "Error on callProduct" + t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    fun callOrder(uId: Int) {
        orderApi.retrieveOrder(uId).enqueue(object : Callback<Order> {
            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                if (response.isSuccessful) {
//                        Toast.makeText(applicationContext,"can find order", Toast.LENGTH_SHORT).show()
                    orderId = response.body()?.id!!.toInt()
                    addressID = response.body()?.user_address_id!!.toInt()

                    println("*****************************************")
                    println(response.body()?.user_address_id.toString())
                    println("*****************************************")
                    if (response.body()?.user_address_id.toString().toInt() == 0) {
                        binding.editAddressProductComplete.text = "กรุณาเลือกที่อยู่"
                    } else {
                        orderApi.orderAddress(addressID).enqueue(object : Callback<AddressClass> {
                            override fun onResponse(call: Call<AddressClass>, response: Response<AddressClass>) {
                                if (response.isSuccessful) {
                                    binding.editAddressProductComplete.text = "ที่อยู่ ${response.body()?.address} " +
                                            "${response.body()?.province} ${response.body()?.district} "+
                                            "\n${response.body()?.zip_code} ${response.body()?.phone}"

                                } else {
                                    Toast.makeText(applicationContext,"cant find address id", Toast.LENGTH_SHORT).show()
                                }
                            }
                            override fun onFailure(call: Call<AddressClass>, t: Throwable) {
                                Toast.makeText(applicationContext,"error on orderAddress" + t.message,Toast.LENGTH_LONG).show()
                            }
                        })
                    }
                    binding.createdAt.text = response.body()?.created_at.toString()
                } else {
                    Toast.makeText(applicationContext, "cant find order id", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Order>, t: Throwable) {
                println(t.message)
                Toast.makeText(applicationContext,"error on callOrder" + t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}