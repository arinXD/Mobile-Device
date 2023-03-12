package com.example.unicode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.unicode.databinding.ActivityAdminEditProductBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AdminEditProduct : AppCompatActivity() {
    private lateinit var bindingEdit : ActivityAdminEditProductBinding
    val createClient = AdminProductAPI.create()
    var one : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingEdit = ActivityAdminEditProductBinding.inflate(layoutInflater)
        setContentView(bindingEdit.root)
        showDropdownone ()
//        intent.putExtra("detail",mDetail)
//        intent.putExtra("photo",mPhoto)
//        intent.putExtra("amount",mAmount)
//        intent.putExtra("subtype_id",mSubtype_id)
        val mId = intent.getStringExtra("id")
        val mName = intent.getStringExtra("product_name")
        val mPrice = intent.getIntExtra("price",0)

        val mDetail = intent.getStringExtra("detail")
        val mPhoto = intent.getStringExtra("photo")
        val mAmount = intent.getIntExtra("amount",0)
        val mSubtype_id = intent.getStringExtra("subtype_id")

        bindingEdit.edtNameProduct.setText(mName)
        bindingEdit.edtPriceProduct.setText(mPrice.toString())
        bindingEdit.edtDetailProduct.setText(mDetail)
        bindingEdit.edtImage.setText(mPhoto.toString())
        bindingEdit.edtSizeS.setText(mAmount.toString())

        bindingEdit.btnAddProduct.setOnClickListener{
            saveProduct()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    //    override fun onResume() {
//        super.onResume()
//        finish()
//    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId== android.R.id.home)
        { finish() }
        return super.onOptionsItemSelected(item)
    }
    fun saveProduct() {
        createClient.updateProduct(
            intent.getStringExtra("id").toString().toInt(),
            bindingEdit.edtNameProduct.text.toString(),
            bindingEdit.edtPriceProduct.text.toString().toInt(),
            bindingEdit.edtDetailProduct.text.toString(),
            bindingEdit.edtImage.text.toString(),
            bindingEdit.edtSizeS.text.toString().toInt(),
            one+1
        ).enqueue(object : Callback<AdminProduct> {
            override fun onResponse(call: Call<AdminProduct>, response: Response<AdminProduct>) {
                if(response.isSuccessful) {
                    Toast.makeText(applicationContext,"Successfully Updated",
                        Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Update Failure",
                        Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<AdminProduct>, t: Throwable) {
                Toast.makeText(applicationContext,"Error onFailure "+ t.message,
                    Toast.LENGTH_LONG).show()
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
                val mSubtype_id = intent.getStringExtra("subtype_id")
                val stationNames = station.map { it.type_name }.toTypedArray()
                val adapter = ArrayAdapter(this, R.layout.dropdown_item, stationNames)
                bindingEdit.autoCompleteTextViewProduct.setText(mSubtype_id)
                bindingEdit.autoCompleteTextViewProduct.setAdapter(adapter)
                bindingEdit.autoCompleteTextViewProduct.setOnItemClickListener { parent, _, position, _ ->
                    one = parent.getItemIdAtPosition(position).toInt()
                }
            },
            onFailure = { error ->
                Log.e("AddTimeStationActivity", "Failed to fetch Station", error)
            }
        )
    }
}