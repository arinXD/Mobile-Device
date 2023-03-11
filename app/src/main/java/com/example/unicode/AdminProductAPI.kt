package com.example.unicode

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface AdminProductAPI {
    @GET("allproduct")
    fun retrieveProduct(): Call<List<AdminProduct>>


    @FormUrlEncoded
    @POST("product")
    fun insertProduct(

        @Field("product_name")product_name: String,
        @Field("price")price: Int,
        @Field("detail")detail: String,
        @Field("photo")photo: String,
        @Field("amount")amount: Int,
        @Field("product_type")product_type: Int): Call<AdminProduct>


    @FormUrlEncoded
    @PUT("product/update")
    fun updateProduct(
        @Field("id")id: Int,
        @Field("product_name")product_name: String,
        @Field("price")price: Int,
        @Field("detail")detail: String,
        @Field("photo")photo: String,
        @Field("amount")amount: Int,
        @Field("product_type")product_type: Int): Call<AdminProduct>


    @DELETE("product/{id}")
    fun daleteProduct(
        @Path("id") id:Int): Call<AdminProduct>


    companion object{
        fun  create(): AdminProductAPI{
            val adminProductClient: AdminProductAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AdminProductAPI::class.java)
            return  adminProductClient
        }
    }
    @GET("allproduct_type")
    fun retrieveProductType(): Call<List<producttypeClass>>

}