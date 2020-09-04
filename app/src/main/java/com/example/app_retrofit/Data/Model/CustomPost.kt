package com.example.app_retrofit.Data.Model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class CustomPost (

    @SerializedName("string--Image")
    @Expose
    var stringImage: String? = null,

    @SerializedName("string--Test--Field")
    @Expose
    var stringTestField: String? = null
)