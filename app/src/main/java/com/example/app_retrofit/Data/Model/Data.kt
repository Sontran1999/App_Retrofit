package com.example.app_retrofit.Data.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Data (
    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("employee_name")
    @Expose
    var name: String? = null,

    @SerializedName("employee_salary")
    @Expose
    var salary: Int? = null,

    @SerializedName("employee_age")
    @Expose
    var age: Int? = null,

    @SerializedName("profile_image")
    @Expose
    var image: String? = null

    )

