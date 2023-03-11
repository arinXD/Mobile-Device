package com.example.unicode

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unicode.databinding.ActivityCraditcardPageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class craditcardPage : AppCompatActivity() {
    private lateinit var binding: ActivityCraditcardPageBinding
    var craditlist = arrayListOf<cradit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCraditcardPageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.recyclerViewCradit.adapter = CraditAdapter(this.craditlist, applicationContext)
        binding.recyclerViewCradit.layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerViewCradit.addItemDecoration(
            DividerItemDecoration(binding.recyclerViewCradit.getContext(),
                DividerItemDecoration.VERTICAL)
        )

        binding.btnAddCradit.setOnClickListener {
            val intent = Intent(applicationContext,InsertCradit::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        callcraditData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun callcraditData(){
        craditlist.clear()
        val serv : CraditAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CraditAPI ::class.java)

        serv.retrieveCradit()
            .enqueue(object : Callback<List<cradit>> {
                override fun onResponse(call: Call<List<cradit>>, response: Response<List<cradit>>) {
                    response.body()?.forEach{
                        craditlist.add(cradit(it.id,it.card_no,it.expire_date,it.cvv,it.firstname))
                    }
                    binding.recyclerViewCradit.adapter = CraditAdapter(craditlist,applicationContext)

                }


                override fun onFailure(call: Call<List<cradit>>, t: Throwable) {
                    Toast.makeText(applicationContext,"Error onFailue " + t.message,
                        Toast.LENGTH_LONG).show()
                }
            })
    }
}