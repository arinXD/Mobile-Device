package com.example.unicode

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class address(
    @Expose
    @SerializedName("id") val id: Int,
    @Expose
    @SerializedName("address") val address: String,
    @Expose
    @SerializedName("province") val province: String,
    @Expose
    @SerializedName("district") val district: String,
    @Expose
    @SerializedName("zip_code") val zip_code: String,
    @Expose
    @SerializedName("phone") val phone: Int,
)