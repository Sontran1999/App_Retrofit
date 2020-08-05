package com.example.app_retrofit.Data.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Data {
    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("salary")
    @Expose
    var salary: String? = null

    @SerializedName("age")
    @Expose
    var age: String? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null

}