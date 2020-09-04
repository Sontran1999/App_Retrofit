package com.example.app_retrofit.Data.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ContactPost (

    @SerializedName("LastName")
    @Expose
    var lastName: String? = null,

    @SerializedName("Email")
    @Expose
    var email: String? = null,

    @SerializedName("custom")
    @Expose
    var custom: CustomPost? = null
){
    @SerializedName("FirstName")
    @Expose
    var firstName: String? = null
}