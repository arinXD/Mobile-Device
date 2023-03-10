package com.example.unicode

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface FavAPI {
    @GET("fav/product/{user_id}")
    fun favProduct(
        @Path("user_id") user_id: Int
    ): Call<List<FavProduct>>

    companion object {
        fun create(): FavAPI {
            val favClient : FavAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FavAPI ::class.java)
            return favClient
        }
    }
}