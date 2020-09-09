package com.example.app_retrofit.ViewModel

import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import android.graphics.ColorSpace
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app_retrofit.Data.Model.Contact
import com.example.app_retrofit.Data.Model.Employee
import com.example.app_retrofit.Data.Model.EmployeePost
import com.example.app_retrofit.Data.Remote.APIService
import com.example.app_retrofit.Data.Remote.ApiUtils
import com.example.app_retrofit.MainActivity
import retrofit2.Response

class RetrofitModel( application: Application) : ViewModel() {
    var employee: MutableLiveData<Employee>
    var employeePostLiveData: MutableLiveData<EmployeePost>
    var mService: APIService? = null

    init {
        employee = MutableLiveData()
        employeePostLiveData = MutableLiveData()
        mService = ApiUtils().getAPIService()
    }

    fun getListDataObserver(): MutableLiveData<Employee> {
        return employee
    }

    fun getAll(mDialog: ProgressDialog) {
        mService?.getAll()?.enqueue(object : retrofit2.Callback<Employee> {
            override fun onFailure(call: retrofit2.Call<Employee>, t: Throwable) {
                Log.d("MainActivity", "error loading from API")
//                Toast.makeText(mContext, "error loading from API", Toast.LENGTH_SHORT)
//                    .show()
//                mDialog?.dismiss()
                employee.postValue(null)
                mDialog?.dismiss()
            }

            override fun onResponse(call: retrofit2.Call<Employee>, response: Response<Employee>) {
                if (response.isSuccessful) {
//                    list = response.body()?.contacts as MutableList<Contact>
//                    mApdaper?.setList(list!!)
                    Log.d("MainActivity", "employees loaded from API")
//                    mDialog?.dismiss()
                    employee.postValue(response.body())
                    mDialog?.dismiss()
                } else {
                    employee.postValue(null)
                }
            }
        })
    }

    fun insert(employeePost: EmployeePost){
        mService?.insert(employeePost)
            ?.enqueue(object : retrofit2.Callback<Contact> {
                override fun onFailure(call: retrofit2.Call<Contact>, t: Throwable) {
                    Log.d("MainActivity", "error");
//                    Toast.makeText(this@NewEmployeeActivity, "save error", Toast.LENGTH_SHORT)
//                        .show()
                    employeePostLiveData.postValue(null)
                }

                override fun onResponse(
                    call: retrofit2.Call<Contact>,
                    response: Response<Contact>
                ) {
                    if (response.isSuccessful) {
                        Log.d("MainActivity", "save successfully")
//                        Toast.makeText(
//                            this@NewEmployeeActivity,
//                            "save successfully",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        finish()
                        employeePostLiveData.postValue(employeePost)
                    } else
                        Log.d("MainActivity", response.message() + response.code())
                        employeePostLiveData.postValue(null)
                }
            })
    }

    fun update(employeePost: EmployeePost){
        mService?.insert(employeePost)
            ?.enqueue(object : retrofit2.Callback<Contact> {
                override fun onFailure(call: retrofit2.Call<Contact>, t: Throwable) {
                    Log.d("MainActivity", "error");
//                    Toast.makeText(this@NewEmployeeActivity, "update error", Toast.LENGTH_SHORT)
                    employeePostLiveData.postValue(null)

                }

                override fun onResponse(
                    call: retrofit2.Call<Contact>,
                    response: Response<Contact>
                ) {
                    if (response.isSuccessful) {
                        Log.d("MainActivity", "update successfully")
//                        Toast.makeText(
//                            this@NewEmployeeActivity,
//                            "update successfully",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        finish()
                        employeePostLiveData.postValue(employeePost)
                    } else
                        Log.d("MainActivity", response.message() + response.code())
                    employeePostLiveData.postValue(null)
                }
            })

    }

    fun delete(){

    }

    class ViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RetrofitModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RetrofitModel(application) as T
            }
            throw IllegalArgumentException("Unable construct viewModel")
        }

    }
}
