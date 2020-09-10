package com.example.app_retrofit.data.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Contact(

    @SerializedName("LastName")
    @Expose
    var lastName: String? = null,

    @SerializedName("Email")
    @Expose
    var email: String? = null,

    @SerializedName("custom_fields")
    @Expose
    var customFields: List<CustomField>? = null

) : Serializable {
    @SerializedName("FirstName")
    @Expose
    var firstName: String? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    @SerializedName("api_originated")
    @Expose
    var apiOriginated: Boolean? = null

    @SerializedName("contact_id")
    @Expose
    var contactId: String? = null

    @SerializedName("Name")
    @Expose
    var name: String? = null
}