package com.example.app_retrofit.Data.Remote


import com.example.app_retrofit.Data.Model.Employee
import retrofit2.http.GET

interface APIService {
    @GET("/employees")
    fun getAll(): retrofit2.Call<Employee>
}