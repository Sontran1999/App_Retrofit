package com.example.app_retrofit.Data.Remote

class ApiUtils {

    val BASE_URL = "http://dummy.restapiexample.com/api/v1/"

    fun getAPIService(): APIService {
        return RetrofitClient.getClient(BASE_URL)!!.create(APIService::class.java)
    }
}