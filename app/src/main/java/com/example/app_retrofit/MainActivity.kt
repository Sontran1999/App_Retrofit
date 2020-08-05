package com.example.app_retrofit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_demo.Adapter.EmployeeListAdapter
import com.example.app_retrofit.Data.Model.Data
import com.example.app_retrofit.Data.Model.Employee
import com.example.app_retrofit.Data.Remote.APIService
import com.example.app_retrofit.Data.Remote.ApiUtils
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    var mService: APIService? = null
    var mApdaper: EmployeeListAdapter? = null
    var list: ArrayList<Data> = ArrayList<Data>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         mService= ApiUtils().getAPIService()
        mApdaper= EmployeeListAdapter(this, list )
        recycler_employee.setHasFixedSize(true)
        recycler_employee.layoutManager = LinearLayoutManager(this)
        recycler_employee.adapter = mApdaper
        load()
    }
    fun load(){
        mService?.getAll()?.enqueue(object : retrofit2.Callback<Employee> {
            override fun onFailure(call: retrofit2.Call<Employee>, t: Throwable) {
                Log.d("MainActivity", "error loading from API");
            }

            override fun onResponse(call: retrofit2.Call<Employee>, response: Response<Employee>) {
                if (response.isSuccessful){
                    mApdaper?.setList(response.body()?.data)
                    Log.d("MainActivity", "posts loaded from API");
                }
            }
        })
    }
}