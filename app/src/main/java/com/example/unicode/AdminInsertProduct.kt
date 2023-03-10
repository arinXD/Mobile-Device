package com.example.unicode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.unicode.databinding.ActivityAdminInsertProductBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminInsertProduct : AppCompatActivity() {
    private lateinit var bindingInsertProduct : ActivityAdminInsertProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingInsertProduct = ActivityAdminInsertProductBinding.inflate(layoutInflater)
        setContentView(bindingInsertProduct.root)

        bindingInsertProduct.btnAddProduct.setOnClickListener {
            addProduct()
        }

        bindingInsertProduct.btnReset.setOnClickListener {
            resetProduct()
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
            bindingInsertProduct.edtPriceSubtypeid.text.toString().toInt()
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
    private fun resetProduct() {
        bindingInsertProduct.edtNameProduct.text?.clear()
        bindingInsertProduct.edtPriceProduct.text?.clear()
        bindingInsertProduct.edtDetailProduct.text?.clear()
        bindingInsertProduct.edtImage.text?.clear()
        bindingInsertProduct.edtSizeS.text?.clear()
        bindingInsertProduct.cdS.clearFocus()
        bindingInsertProduct.edtPriceSubtypeid.text?.clear()
//        bindingInsertProduct.cdM.clearFocus()
//        bindingInsertProduct.cdL.clearFocus()
    }
}