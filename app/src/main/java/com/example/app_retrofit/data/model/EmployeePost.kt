package com.example.app_retrofit.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EmployeePost (
    @SerializedName("contact")
    @Expose
    var contact: ContactPost? = null
)