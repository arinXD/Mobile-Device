package com.example.unicode

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface OrderAPI {

    @GET("myOrder/{id}")
    fun retrieveOrder(
        @Path("id") id: Int
    ): Call<Order>

    @FormUrlEncoded
    @POST("addOrder")
    fun addOrder(
        @Field("user_id") user_id:Int,
        @Field("amount") amount: Int,
        @Field("product_price") product_price: Int,
        @Field("price_all") price_all: Int,
        @Field("order_id") order_id: Int,
        @Field("product_id") product_id: Int,
        @Field("size_id") size_id: Int,
    ):Call<OrderDetail>

    companion object {
        fun create(): OrderAPI {
            val Client : OrderAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OrderAPI ::class.java)
            return Client
        }
    }
}