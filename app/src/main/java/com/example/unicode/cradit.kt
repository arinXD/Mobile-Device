package com.example.unicode

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class cradit(
    @Expose
    @SerializedName("id") val id: Int,
    @Expose
    @SerializedName("card_no") val card_no: String,
    @Expose
    @SerializedName("expire_date") val expire_date: String,
    @Expose
    @SerializedName("CVV") val cvv: Int,
    @Expose
    @SerializedName("firstname") val firstname: String,
)
