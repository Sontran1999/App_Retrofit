package com.example.app_retrofit.Data.Remote


import com.example.app_retrofit.Data.Model.Data
import com.example.app_retrofit.Data.Model.Employee
import retrofit2.http.*

interface APIService {
    @Headers(
        value = ["Accept: application/json",
            "Content-type:application/json"]
    )
    @GET("employees/")
    fun getAll(): retrofit2.Call<Employee>


    @POST("create/")
    @FormUrlEncoded
    fun insert(
        @Field("id") id: Int,
        @Field("employee_name") name: String,
        @Field("employee_salary") salary: Int,
        @Field("employee_age") age: Int,
        @Field("profile_image") image: String
    ): retrofit2.Call<Data>
}