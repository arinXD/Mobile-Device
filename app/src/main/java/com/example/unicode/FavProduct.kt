package com.example.unicode

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FavProduct(
    @Expose
    @SerializedName("id") val id: Int,

    @Expose
    @SerializedName("product_name") val product_name: String,

    @Expose
    @SerializedName("price") val price: Int,

    @Expose
    @SerializedName("photo") val photo: String,
)
