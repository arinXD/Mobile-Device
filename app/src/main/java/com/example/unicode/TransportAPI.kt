package com.example.unicode

import retrofit2.Call
import retrofit2.http.*

interface TransportAPI {
    @GET("alltransport")
    fun retrieveTransport(): Call<List<transport>>

    @FormUrlEncoded
    @POST("transport")
    fun insertTransport(
//        @Field("id") id: String,
        @Field("detail") detail: String,
        @Field("transport_status_id") transport_status_id: Int,
        @Field("order_id") order_id: Int ): Call<transport>

    @GET("alltransport_status")
    fun retrieveTransporttt(): Call<List<transport_status>>
}