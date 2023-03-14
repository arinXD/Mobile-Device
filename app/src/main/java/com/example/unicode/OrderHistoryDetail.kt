package com.example.unicode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.unicode.databinding.ActivityOrderHistoryDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class OrderHistoryDetail : AppCompatActivity() {
    private lateinit var binding: ActivityOrderHistoryDetailBinding
    lateinit var session: SessionManager
    var orderId = 0
    var orderAPI = OrderAPI.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        session = SessionManager(applicationContext)
        val userName: String? = session.pref.getString(SessionManager.KEY_NAME, null)
        binding = ActivityOrderHistoryDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        orderId = intent.getIntExtra("orderId", 0)

        binding.userName.text = userName
    }

    override fun onResume() {
        super.onResume()
        findOrder(orderId)
        var priceAll = 0
        orderAPI.findOrderDetail(orderId).enqueue(object : Callback<List<OrderDetail>> {
            override fun onResponse(call: Call<List<OrderDetail>>, response: Response<List<OrderDetail>>) {
                if (response.isSuccessful) {
                    response.body()?.forEach {
                        priceAll+= it.price_all
                    }
                    binding.priceTxt.text = "รวมทั้งหมด: ${priceAll}"
                } else {
                    Toast.makeText(applicationContext, "cant find order id", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<OrderDetail>>, t: Throwable) {
                println(t.message)
                Toast.makeText(applicationContext, "error on failed" + t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
    //    create menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun findOrder(orderId: Int) {
        orderAPI.findOrder(orderId).enqueue(object : Callback<Order> {
            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                if (response.isSuccessful) {
                    binding.dateTxt.text =
                        "สั่งซื้อวันที่: ${formatDate(response.body()?.created_at.toString())}"

//                    address
                    orderAPI.orderAddress(response.body()?.user_address_id.toString().toInt())
                        .enqueue(object : Callback<AddressClass> {
                            override fun onResponse(
                                call: Call<AddressClass>,
                                response: Response<AddressClass>
                            ) {
                                if (response.isSuccessful) {
                                    var addressData = response.body()
                                    binding.addressDetail.text = "${addressData?.address}" +
                                            "\n${addressData?.province}" +
                                            "\n${addressData?.district}" +
                                            "\n${addressData?.zip_code}" +
                                            "\n${addressData?.phone}"
                                } else {
                                    Toast.makeText(
                                        applicationContext,
                                        "cant find order id",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<AddressClass>, t: Throwable) {
                                Toast.makeText(
                                    applicationContext,
                                    "error on failed" + t.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })
//                    credit
                    orderAPI.orderCredit(response.body()?.credit_card_id.toString().toInt())
                        .enqueue(object : Callback<Credit> {
                            override fun onResponse(
                                call: Call<Credit>,
                                response: Response<Credit>
                            ) {
                                if (response.isSuccessful) {
                                    var creditData = response.body()
                                    binding.cardDetail.text =
                                        "Name: ${creditData?.firstname}" +
                                        "\nCard no: ${getMaskedCardNumber(creditData?.card_no.toString())}" +
                                        "\nExpire date: ${formatDate(creditData?.expire_date.toString())}" +
                                        "\nCVV: ${getMaskedCVV(creditData?.cvv.toString().toInt())}"
                                } else {
                                    Toast.makeText(
                                        applicationContext,
                                        "cant find order id",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<Credit>, t: Throwable) {
                                Toast.makeText(
                                    applicationContext,
                                    "error on failed" + t.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })

                } else {
                    Toast.makeText(applicationContext, "cant find order id", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<Order>, t: Throwable) {
                println(t.message)
                Toast.makeText(applicationContext, "error on failed" + t.message, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun formatDate(dateString: String): String {
        // Parse the date string and format it to "yyyy-MM-dd"
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }

    private fun getMaskedCardNumber(cardNumber: String): String {
        // Replace all but the last 4 digits with asterisks
        val masked = cardNumber.replace(Regex("\\d(?=\\d{4})"), "*")
        return masked
    }

    private fun getMaskedCVV(cvv: Int): String {
        // Create a string of asterisks with the same length as the CVV number
        val masked = "*".repeat(cvv.toString().length)
        return masked
    }
}