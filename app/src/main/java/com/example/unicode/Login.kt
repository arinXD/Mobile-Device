package com.example.unicode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.unicode.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        session = SessionManager(applicationContext)
        if (session.isLoggedIn()) {
            if(session.getType()=="user"){
                var i: Intent = Intent(applicationContext, AllProducts::class.java)
                startActivity(i)
            }else{
                var i: Intent = Intent(applicationContext, AdminPage::class.java)
                startActivity(i)
            }
            finish()
        }
        if (!session.pref.getString(SessionManager.KEY_EMAIL, null).isNullOrEmpty()) {
            val email: String? = session.pref.getString(SessionManager.KEY_EMAIL, null)
            binding.userEmail.setText(email)
        }

//        var userEmail = binding.userEmail.text.toString()
//        var password = binding.userPassword.text.toString()

        binding.login.setOnClickListener {
            if (binding.userEmail.text.toString().isEmpty() || binding.userPassword.text.toString()
                    .isEmpty()
            ) {
                Toast.makeText(
                    applicationContext,
                    "Enter username and password.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                login()
            }
        }

        binding.register.setOnClickListener {
            val intent = Intent(applicationContext, Register::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun login() {
        val api = UserAPI.create()
        api.login(
            binding.userEmail.text.toString(),
            binding.userPassword.text.toString()
        ).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                val status = response.body()?.status.toString().toInt()
                if (status == 0) {
                    Toast.makeText(
                        applicationContext,
                        "The username or password is incorrect",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    val id = response.body()?.user_id.toString()
                    val type = response.body()?.user_type.toString()
                    val username = response.body()?.user_name.toString()
                    val email = response.body()?.email.toString()
                    val gender = response.body()?.gender.toString()
                    val bDay = response.body()?.birthday.toString()
//                    id: String,
//                    type: String,
//                    username: String,
//                    email: String,
//                    gender: String,
//                    bDay: String
                    session.createLoginSession(
                        id,
                        type,
                        username,
                        email,
                        gender,
                        bDay
                    )
                    var i: Intent
                    if(type=="user"){
                        Toast.makeText(applicationContext, type, Toast.LENGTH_LONG).show()
                        i = Intent(applicationContext, AllProducts::class.java)
                        startActivity(i)
                    }else{
                        Toast.makeText(applicationContext, type, Toast.LENGTH_LONG).show()
                        i = Intent(applicationContext, AdminPage::class.java)
                        startActivity(i)
                    }
                    finish()
                }
//                if (response.isSuccessful) {
//                    Toast.makeText(applicationContext,"Login",
//                        Toast.LENGTH_SHORT).show()
//                }
//                    var intent = Intent(applicationContext,AllProducts::class.java)
//                    var userResponse = response.body()
//                    //"status": 1,
//                    //"user_id": 2,
//                    //"user_type": "user",
//                    //"user_name": "oat",
//                    //"email": "oat@gmail.com",
//                    //"user_password": "1234",
//                    //"gender": "1234",
//                    //"birthday": "2022-08-31T17:00:00.000Z"
//                    var userData = ArrayList<String>()
//                    userData.add(userResponse?.user_id.toString())
//                    userData.add(userResponse?.user_type.toString())
//                    userData.add(userResponse?.user_name.toString())
//                    userData.add(userResponse?.email.toString())
//                    userData.add(userResponse?.user_password.toString())
//                    userData.add(userResponse?.gender.toString())
//                    userData.add(userResponse?.birthday.toString())
//                    intent.putStringArrayListExtra("userData",userData)
//                    startActivity(intent)
//                    finish()
//
//                } else {
//                    Toast.makeText(applicationContext,"Login Failed",
//                        Toast.LENGTH_SHORT).show()
//
//                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(
                    applicationContext, "Error onFailure " +
                            t.message, Toast.LENGTH_LONG
                ).show()
            }

        })

    }
}