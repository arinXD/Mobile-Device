package com.example.unicode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.unicode.databinding.ActivityShowEditBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowEditActivity : AppCompatActivity() {
    private lateinit var bindingEdit: ActivityShowEditBinding
    val createClient = AddressAPI.create()
    var mId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingEdit = ActivityShowEditBinding.inflate(layoutInflater)
        setContentView(bindingEdit.root)
        mId = intent.getStringExtra("mId").toString()
        val mAddress = intent.getStringExtra("mAddress")
        val mProvince = intent.getStringExtra("mProvince")
        val mDistrict = intent.getStringExtra("mDistrict")
        val mZipcode = intent.getStringExtra("mZip_code")
        val mPhone = intent.getStringExtra("mPhone")
        val mIDs = intent.getStringExtra("mId")
        bindingEdit.editaddress.setText(mAddress)
        bindingEdit.editprovince.setText(mProvince)
        bindingEdit.editdistrict.setText(mDistrict)
        bindingEdit.editzipCode.setText(mZipcode)
        bindingEdit.editphone.setText(mPhone)
    }

    fun saveStudent(v: View) {
        createClient.updateAddress(
            mId.toInt(),
            bindingEdit.editaddress.text.toString(),
            bindingEdit.editprovince.text.toString(),
            bindingEdit.editdistrict.text.toString(),
            bindingEdit.editzipCode.text.toString(),
            bindingEdit.editphone.text.toString().toInt(),
            1
        ).enqueue(object : Callback<address> {
            override fun onResponse(call: Call<address>, response: Response<address>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        applicationContext, "Seccessfully Updated",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        applicationContext, "Update Failure",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<address>, t: Throwable) {
                Toast.makeText(
                    applicationContext, "Update Failure",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}