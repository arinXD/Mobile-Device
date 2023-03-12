package com.example.unicode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.unicode.databinding.ActivityAccountPageBinding

class AccountPage : AppCompatActivity() {
    private lateinit var binding : ActivityAccountPageBinding
    lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountPageBinding.inflate(layoutInflater)
        session = SessionManager(applicationContext)
        setContentView(binding.root)

        val email: String? = session.pref.getString(SessionManager.KEY_EMAIL, null)
        val userName: String? = session.pref.getString(SessionManager.KEY_NAME, null)

        binding.userName.text = userName
        binding.email.text = email

        binding.myOrder.setOnClickListener {
//            var intent = Intent(applicationContext, ::class.java)
//            startActivity(intent)
        }
        binding.myCredit.setOnClickListener {
            var intent = Intent(applicationContext, craditcardPage::class.java)
            startActivity(intent)
        }
        binding.myFavorite.setOnClickListener {
            var intent = Intent(applicationContext, FavoritePage::class.java)
            startActivity(intent)
        }
        binding.myAddress.setOnClickListener {
            var intent = Intent(applicationContext, addressPage::class.java)
            startActivity(intent)
        }
        binding.myTransport.setOnClickListener {
//            var intent = Intent(applicationContext, ::class.java)
//            startActivity(intent)
        }

        binding.logout.setOnClickListener {
            val edit = session.edior
            edit.clear()
            edit.commit()

            edit.putString(SessionManager.KEY_EMAIL,email)
            edit.commit()

            var intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.bottomNavigation.setOnItemSelectedListener {

            when (it.itemId){
                R.id.category -> Toast.makeText(applicationContext, "Category", Toast.LENGTH_LONG).show()
                R.id.home ->{
                    intent = Intent(applicationContext,AllProducts::class.java)
//                    intent.putStringArrayListExtra("userData",userData)
                    startActivity(intent)
                }
                R.id.account -> {
                    ""
                }
            }
            true
        }
    }
    override fun onResume() {
        super.onResume()
    }

    //    create menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId== android.R.id.home) {
            finish()
        }
        when (item.itemId) {
            R.id.fav -> {
                startActivity(Intent(applicationContext, FavoritePage::class.java))
            }
            R.id.basket -> Toast.makeText(applicationContext, "Basket", Toast.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }
}