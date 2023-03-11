package com.example.unicode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.unicode.DatePicker
import com.example.unicode.databinding.ActivityInsertCraditBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InsertCradit : AppCompatActivity() {
    private lateinit var bindingInsert: ActivityInsertCraditBinding
    var craditlist = arrayListOf<cradit>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingInsert = ActivityInsertCraditBinding.inflate(layoutInflater)
        setContentView(bindingInsert.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bindingInsert.btnAddCradit.setOnClickListener{
            addCradit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun showDatePickerDialog(v: View) {
        val newDateFragment = DatePicker()
        newDateFragment.show(supportFragmentManager, "Date Picker")
    }
    private fun addCradit(){
        val api: CraditAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CraditAPI::class.java)
        api.insertCradit(
            bindingInsert.editName.text.toString(),
            bindingInsert.editCardID.text.toString(),
            bindingInsert.editexpiredate.text.toString(),
            bindingInsert.editcvv.text.toString().toInt(),1
        ).enqueue(object : Callback<cradit> {
            override fun onResponse(
                call: Call<cradit>,
                response: retrofit2.Response<cradit>
            ) {
                if (response.isSuccessful()){
                    Toast.makeText(applicationContext,"Successfully Inserted", Toast.LENGTH_SHORT).show()
                    finish()
                }
                else{
                    Toast.makeText(applicationContext,"Inserted Failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<cradit>, t: Throwable) {
                Toast.makeText(applicationContext,"Error onFailuse "+t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}