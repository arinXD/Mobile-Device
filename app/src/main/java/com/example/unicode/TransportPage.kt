package com.example.unicode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unicode.databinding.ActivityCraditcardPageBinding
import com.example.unicode.databinding.ActivityTransportPageBinding
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TransportPage : AppCompatActivity() {
    private lateinit var binding: ActivityTransportPageBinding
    var transportlist = arrayListOf<order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTransportPageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.recyclerViewTransport.adapter = TransportAdapter(this.transportlist, applicationContext)
        binding.recyclerViewTransport.layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerViewTransport.addItemDecoration(
            DividerItemDecoration(
                binding.recyclerViewTransport.getContext(),
                DividerItemDecoration.VERTICAL
            )
        )

    }
    override fun onResume() {
        super.onResume()
        callTransportData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun callTransportData(){
        transportlist.clear()
        val serv : OrderAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrderAPI ::class.java)
        serv.retrieveOrder()
            .enqueue(object : Callback<List<order>> {
                override fun onResponse(call: Call<List<order>>, response: Response<List<order>>) {
                    response.body()?.forEach{
                        transportlist.add(order(it.id,it.pay_status,it.transport_fee,it.user_address_id,it.user_id,it.created_at,it.updated_at))
                    }
                    binding.recyclerViewTransport.adapter = TransportAdapter(transportlist,applicationContext)
                }

                override fun onFailure(call: Call<List<order>>, t: Throwable) {
                    Toast.makeText(applicationContext,"Error onFailue " + t.message,
                        Toast.LENGTH_LONG).show()
                }
            })
    }

}