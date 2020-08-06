package com.example.app_retrofit.Data.Model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


data class Employee(
    @SerializedName("status")
    @Expose
    var status: String? = null,

    @SerializedName("data")
    @Expose
    var data: List<Data>? = null,

    @SerializedName("message")
    @Expose
    var message: String? = null

)