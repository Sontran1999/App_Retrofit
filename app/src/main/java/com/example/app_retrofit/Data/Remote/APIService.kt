package com.example.app_retrofit.Data.Remote


import com.example.app_retrofit.Data.Model.*
import retrofit2.http.*

interface APIService {
    @GET("contacts/bookmark")
    fun getAll(): retrofit2.Call<Employee>

    @POST("contact")
    fun insert(
        @Body employeePost: EmployeePost
    ): retrofit2.Call<Contact>

//    @Headers(
//        value = ["Accept: application/json",
//            "Content-type:application/json",
//            "autopilotapikey:116553be00eb43e19c3fd63890b1b98e"]
//    )
//    @Multipart
//    @PUT("update/{id}")
//    fun update(@Part("id") id: Int,
//               @Part("employee_name") name: String,
//               @Part("employee_salary") salary: Int,
//               @Part("employee_age") age: Int,
//               @Part("profile_image") image: String): retrofit2.Call<EmployeePost>

    @DELETE("contact/{id}")
    fun delete(@Path("id") id: String?): retrofit2.Call<Unit>
}