package com.example.unicode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter

import com.example.unicode.databinding.ActivityAddressPageBinding
import com.example.unicode.databinding.ActivityCategoryPageBinding

class categoryPage : AppCompatActivity() {


    private lateinit var binding: ActivityCategoryPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user= arrayOf("sdsds","sqsqqs","dsdsdqs","dsfwq","dsfwqqqq","dsfwsdsdsq","dsgfgfsfwq","dsfgggwq","dsqsqfwq")
        val userAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,android.R.layout.simple_list_item_1,user
        )
        binding.idListView.adapter=userAdapter;

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    userAdapter.filter.filter(null)
                } else {
                    userAdapter.filter.filter(newText)
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) {
                    userAdapter.filter.filter(null)
                } else {
                    userAdapter.filter.filter(query)
                }
                return true
            }
        })
    }
}