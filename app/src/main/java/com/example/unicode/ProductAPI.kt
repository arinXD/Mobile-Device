package com.example.unicode

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductAPI {
    @GET("products/all")
    fun productAll(): Call<List<ProductClass>>

    @GET("products/last")
    fun productLast(): Call<List<ProductClass>>

    @GET("products/size/{id}")
    fun findProductSize(
        @Path("id") id: Int
    ): Call<List<SizeClass>>

    companion object {
        fun create(): ProductAPI {
            val productClient : ProductAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ProductAPI ::class.java)
            return productClient
        }
    }

}