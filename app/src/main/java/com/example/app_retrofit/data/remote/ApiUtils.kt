package com.example.app_retrofit.data.remote

class ApiUtils {

    val BASE_URL = "https://private-amnesiac-8522d-autopilot.apiary-proxy.com/v1/"

    fun getAPIService(): APIService {
        return RetrofitClient.getClient(BASE_URL)!!.create(APIService::class.java)
    }
}