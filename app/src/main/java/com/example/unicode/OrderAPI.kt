package com.example.unicode

import retrofit2.Call
import retrofit2.http.GET

interface OrderAPI {
    @GET("allorderss")
    fun retrieveOrder(): Call<List<order>>
}