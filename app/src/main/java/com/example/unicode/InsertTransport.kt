package com.example.unicode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.unicode.R
import com.example.unicode.databinding.ActivityInsertCraditBinding
import com.example.unicode.databinding.ActivityInsertTransportBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InsertTransport : AppCompatActivity() {
    private lateinit var bindingInsert: ActivityInsertTransportBinding
    var transportv2list = arrayListOf<transport>()
    var one : Int = 0
    var two = ""
    var three = ""
    var four = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingInsert = ActivityInsertTransportBinding.inflate(layoutInflater)
        setContentView(bindingInsert.root)
        showDropdownone()

        bindingInsert.btnAddTransport.setOnClickListener{
            addTransport()
        }
    }

    fun getVan(onSuccess: (List<transport_status>) -> Unit, onFailure: (Throwable) -> Unit) {
        val api:TransportAPI= Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TransportAPI::class.java)
        api.retrieveTransporttt().enqueue(object : Callback<List<transport_status>> {
            override fun onResponse(call: Call<List<transport_status>>, response: Response<List<transport_status>>) {
                if (response.isSuccessful) {
                    val van = response.body()
                    if (van != null) {
                        onSuccess(van)
                    }
                } else {
                    onFailure(Throwable("Failed to fetch cities"))
                }
            }

            override fun onFailure(call: Call<List<transport_status>>, t: Throwable) {
                onFailure(t)
            }
        })

    }

    private fun showDropdownone () {
        getVan(
            onSuccess = { station ->
                val stationNames = station.map { it.title }.toTypedArray()
                val adapter = ArrayAdapter(this, R.layout.dropdown_item, stationNames)
                bindingInsert.autoCompleteTextViewTransport.setText("สั่งซื้อสินค้า")
                bindingInsert.autoCompleteTextViewTransport.setAdapter(adapter)
                bindingInsert.autoCompleteTextViewTransport.setOnItemClickListener { parent, _, position, _ ->
                    one = parent.getItemIdAtPosition(position).toInt()
                }
            },
            onFailure = { error ->
                Log.e("AddTimeStationActivity", "Failed to fetch Station", error)
            }
        )
    }
    private fun showDropdowntwo () {
        getVan(
            onSuccess = { station ->
                val stationNames = station.map { it.title }.toTypedArray()
                val adapter = ArrayAdapter(this, R.layout.dropdown_item, stationNames)
                bindingInsert.autoCompleteTextViewTransport.setText("กำลังเตรียมพัสดุ")
                bindingInsert.autoCompleteTextViewTransport.setAdapter(adapter)
                bindingInsert.autoCompleteTextViewTransport.setOnItemClickListener { parent, _, position, _ ->
                    two = parent.getItemIdAtPosition(position).toInt().toString()
                }
            },
            onFailure = { error ->
                Log.e("AddTimeStationActivity", "Failed to fetch Station", error)
            }
        )
    }
    private fun showDropdownthree () {
        getVan(
            onSuccess = { station ->
                val stationNames = station.map { it.title }.toTypedArray()
                val adapter = ArrayAdapter(this, R.layout.dropdown_item, stationNames)
                bindingInsert.autoCompleteTextViewTransport.setText("กำลังนำส่ง")
                bindingInsert.autoCompleteTextViewTransport.setAdapter(adapter)
                bindingInsert.autoCompleteTextViewTransport.setOnItemClickListener { parent, _, position, _ ->
                    three = parent.getItemIdAtPosition(position).toInt().toString()
                }
            },
            onFailure = { error ->
                Log.e("AddTimeStationActivity", "Failed to fetch Station", error)
            }
        )
    }
    private fun showDropdownfour () {
        getVan(
            onSuccess = { station ->
                val stationNames = station.map { it.title }.toTypedArray()
                val adapter = ArrayAdapter(this, R.layout.dropdown_item, stationNames)
                bindingInsert.autoCompleteTextViewTransport.setText("จัดส่งเสร็จสิ้น")
                bindingInsert.autoCompleteTextViewTransport.setAdapter(adapter)
                bindingInsert.autoCompleteTextViewTransport.setOnItemClickListener { parent, _, position, _ ->
                    four = parent.getItemIdAtPosition(position).toInt().toString()
                }
            },
            onFailure = { error ->
                Log.e("AddTimeStationActivity", "Failed to fetch Station", error)
            }
        )
    }
    private fun addTransport(){
        val api: TransportAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TransportAPI::class.java)
        api.insertTransport(
            bindingInsert.editTexttransport.text.toString(),one+1,2
        ).enqueue(object : Callback<transport> {
            override fun onResponse(
                call: Call<transport>,
                response: retrofit2.Response<transport>
            ) {
                if (response.isSuccessful()){
                    Toast.makeText(applicationContext,"Successfully Inserted", Toast.LENGTH_SHORT).show()
                    finish()
                }
                else{
                    Toast.makeText(applicationContext,"Inserted Failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<transport>, t: Throwable) {
                Toast.makeText(applicationContext,"Error onFailuse "+t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

}