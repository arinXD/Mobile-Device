package com.example.unicode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.unicode.databinding.ActivityShoppingBagBinding

class ShoppingBag : AppCompatActivity() {
    private lateinit var binding: ActivityShoppingBagBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityShoppingBagBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}