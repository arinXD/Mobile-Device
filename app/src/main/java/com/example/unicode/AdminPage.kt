package com.example.unicode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unicode.databinding.ActivityAdminPageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminPage : AppCompatActivity(), AdminProductsAdapter.MyClickListener{
    private lateinit var binding : ActivityAdminPageBinding
    var productList = arrayListOf<AdminProduct>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.recyclerView.adapter = AdminProductsAdapter(this.productList, applicationContext,this@AdminPage)
        binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerView.layoutManager = GridLayoutManager(this,2)
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(binding.recyclerView.getContext(), DividerItemDecoration.VERTICAL)
        )
        binding.btnAddProduct.setOnClickListener {
            val intent = Intent(applicationContext, AdminInsertProduct::class.java)
            startActivity(intent)
        }

    }
    override  fun onResume() {
        super.onResume()
        callProductData()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var session = SessionManager(applicationContext)
        if(item.itemId== android.R.id.home) {
            val email: String? = session.pref.getString(SessionManager.KEY_EMAIL, null)
            val edit = session.edior
            edit.clear()
            edit.commit()

            edit.putString(SessionManager.KEY_EMAIL,email)
            edit.commit()

            var intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun callProductData() {
        productList.clear();
        val serv = AdminProductAPI.create()
        serv.retrieveProduct()
            .enqueue(object : Callback<List<AdminProduct>> {
                override fun onResponse(
                    call: Call<List<AdminProduct>>,
                    response: Response<List<AdminProduct>>
                ) {
                    response.body()?.forEach {
                        productList.add(AdminProduct(it.id,it.product_name,it.price,it.detail,it.photo,it.amount,it.product_type))
                    }
                    binding.recyclerView.adapter = AdminProductsAdapter(productList, applicationContext,this@AdminPage)

                }

                override fun onFailure(call: Call<List<AdminProduct>>, t: Throwable) {
                    Toast.makeText(applicationContext,"Error onFailure "+ t.message, Toast.LENGTH_LONG).show()
                }
            })
    }

    override fun onClick(position: Int) {
        println(productList[position])
        var product = productList[position]
        val intent = Intent(this, AdminShowEditDelete::class.java)
        intent.putExtra("id", product.id.toString().toInt())
        intent.putExtra("product_name", product.product_name)
        intent.putExtra("price", product.price.toString().toInt())
        intent.putExtra("amount", product.amount.toString().toInt())
        intent.putExtra("detail", product.detail)
        intent.putExtra("product_type", product.product_type.toString().toInt())
        intent.putExtra("photo", product.photo)
        startActivity(intent)
//        startActivity(Intent(this,ShowEditDeleteActivity::class.java))
    }


}