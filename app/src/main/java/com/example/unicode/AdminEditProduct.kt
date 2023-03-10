package com.example.unicode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.unicode.databinding.ActivityAdminEditProductBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminEditProduct : AppCompatActivity() {
    private lateinit var bindingEdit : ActivityAdminEditProductBinding
    val createClient = AdminProductAPI.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingEdit = ActivityAdminEditProductBinding.inflate(layoutInflater)
        setContentView(bindingEdit.root)

//        intent.putExtra("detail",mDetail)
//        intent.putExtra("photo",mPhoto)
//        intent.putExtra("amount",mAmount)
//        intent.putExtra("subtype_id",mSubtype_id)

        val mId = intent.getIntExtra("id",0)
        val mName = intent.getStringExtra("product_name")
        val mPrice = intent.getIntExtra("price",0)

        val mDetail = intent.getStringExtra("detail")
        val mPhoto = intent.getStringExtra("photo")
        val mAmount = intent.getIntExtra("amount",0)
        val mSubtype_id = intent.getStringExtra("subtype_id")

        bindingEdit.btnid.setText(mId.toString())
        bindingEdit.btnid.isEnabled = false
        bindingEdit.edtNameProduct.setText(mName)
        bindingEdit.edtPriceProduct.setText(mPrice.toString())
        bindingEdit.edtDetailProduct.setText(mDetail)
        bindingEdit.edtImage.setText(mPhoto.toString())
        bindingEdit.edtSizeS.setText(mAmount.toString())
        bindingEdit.edtPriceSubtypeid.setText(mSubtype_id.toString())

        bindingEdit.btnReset.setOnClickListener {
            resetProduct()
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
    fun saveProduct(v: View) {
        createClient.updateProduct(
            bindingEdit.btnid.text.toString().toInt(),
            bindingEdit.edtNameProduct.text.toString(),
            bindingEdit.edtPriceProduct.text.toString().toInt(),
            bindingEdit.edtDetailProduct.text.toString(),
            bindingEdit.edtImage.text.toString(),
            bindingEdit.edtSizeS.text.toString().toInt(),
            bindingEdit.edtPriceSubtypeid.text.toString().toInt()
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
    private fun resetProduct() {
        bindingEdit.edtNameProduct.text?.clear()
        bindingEdit.edtPriceProduct.text?.clear()
        bindingEdit.edtDetailProduct.text?.clear()
        bindingEdit.edtImage.text?.clear()
        bindingEdit.edtSizeS.text?.clear()
        bindingEdit.cdS.clearFocus()
        bindingEdit.edtPriceSubtypeid.text?.clear()
//        bindingInsertProduct.cdM.clearFocus()
//        bindingInsertProduct.cdL.clearFocus()
    }
}