package com.example.unicode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.unicode.databinding.ActivityAdminInsertProductBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AdminInsertProduct : AppCompatActivity() {
    private lateinit var bindingInsertProduct : ActivityAdminInsertProductBinding
    var one : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingInsertProduct = ActivityAdminInsertProductBinding.inflate(layoutInflater)
        setContentView(bindingInsertProduct.root)
        showDropdownone ()

        bindingInsertProduct.btnAddProduct.setOnClickListener {
            addProduct()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId== android.R.id.home)
        { finish() }
        return super.onOptionsItemSelected(item)
    }
    private fun addProduct() {
        val api = AdminProductAPI.create()
        api.insertProduct(
            bindingInsertProduct.edtNameProduct.text.toString(),
            bindingInsertProduct.edtPriceProduct.text.toString().toInt(),
            bindingInsertProduct.edtDetailProduct.text.toString(),
            bindingInsertProduct.edtImage.text.toString(),
            bindingInsertProduct.edtSizeS.text.toString().toInt(),
            one+1
        ).enqueue(object: Callback<AdminProduct> {
            override fun onResponse(
                call: Call<AdminProduct>,
                response: Response<AdminProduct>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext,"Succesfully Inserted",
                        Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(applicationContext,"Inserted Failed",
                        Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<AdminProduct>, t: Throwable) {
                Toast.makeText(applicationContext,"Error onFailure "+
                        t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
    fun callprotype(onSuccess: (List<producttypeClass>) -> Unit, onFailure: (Throwable) -> Unit) {
        val api:AdminProductAPI= Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AdminProductAPI::class.java)
        api.retrieveProductType().enqueue(object : Callback<List<producttypeClass>> {
            override fun onResponse(call: Call<List<producttypeClass>>, response: Response<List<producttypeClass>>) {
                if (response.isSuccessful) {
                    val van = response.body()
                    if (van != null) {
                        onSuccess(van)
                    }
                } else {
                    onFailure(Throwable("Failed to fetch cities"))
                }
            }

            override fun onFailure(call: Call<List<producttypeClass>>, t: Throwable) {
                onFailure(t)
            }
        })

    }
    private fun showDropdownone () {
        callprotype(
            onSuccess = { station ->
                val stationNames = station.map { it.type_name }.toTypedArray()
                val adapter = ArrayAdapter(this, R.layout.dropdown_item, stationNames)
                bindingInsertProduct.autoCompleteTextViewProduct.setText("สั่งซื้อสินค้า")
                bindingInsertProduct.autoCompleteTextViewProduct.setAdapter(adapter)
                bindingInsertProduct.autoCompleteTextViewProduct.setOnItemClickListener { parent, _, position, _ ->
                    one = parent.getItemIdAtPosition(position).toInt()
                }
            },
            onFailure = { error ->
                Log.e("AddTimeStationActivity", "Failed to fetch Station", error)
            }
        )
    }
}