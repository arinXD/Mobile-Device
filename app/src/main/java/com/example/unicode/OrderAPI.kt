package com.example.unicode

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface OrderAPI {
    //    /order/product/:user_id
    @GET("order/product/{user_id}")
    fun callProduct(
        @Path("user_id") user_id: Int
    ): Call<List<OrderProductClass>>
//    /order/:address_id
    @GET("/order/{address_id}")
    fun orderAddress(
        @Path("address_id") id: Int
    ): Call<AddressClass>

    @GET("myOrder/{id}")
    fun retrieveOrder(
        @Path("id") id: Int
    ): Call<Order>

    @FormUrlEncoded
    @POST("addOrder")
    fun addOrder(
        @Field("user_id") user_id: Int,
        @Field("amount") amount: Int,
        @Field("product_price") product_price: Int,
        @Field("price_all") price_all: Int,
        @Field("order_id") order_id: Int,
        @Field("product_id") product_id: Int,
        @Field("size_id") size_id: Int,
    ): Call<OrderDetail>

    @DELETE("delete/order/{order_datial_id}")
    fun deleteOrderDetail(
        @Path("order_datial_id") id: Int
    ): Call<OrderProductClass>

    @PUT("order/address/{order_id}/{address_id}")
    fun updateOrderAddress(
        @Path("order_id") order_id: Int,
        @Path("address_id") address_id: Int,
    ): Call<Order>

    companion object {
        fun create(): OrderAPI {
            val Client: OrderAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OrderAPI::class.java)
            return Client
        }
    }
}