package com.example.unicode

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserAPI {

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("user_password") user_password: String
    ):Call<User>

    @FormUrlEncoded
    @POST("register")
    fun insertUser(
        @Field("user_name") user_name:String,
        @Field("email") email: String,
        @Field("user_password") user_password:Int,
        @Field("gender") gender: String,
        @Field("birthday") birthday: String
    ):Call<AddUser>

    companion object {
        fun create(): UserAPI {
            val userClient : UserAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UserAPI ::class.java)
            return userClient
        }
    }

}