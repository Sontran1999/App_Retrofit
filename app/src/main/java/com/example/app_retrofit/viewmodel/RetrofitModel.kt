package com.example.app_retrofit.viewmodel

import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface


import android.graphics.ColorSpace
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app_retrofit.data.model.Contact
import com.example.app_retrofit.data.model.Employee
import com.example.app_retrofit.data.model.EmployeePost
import com.example.app_retrofit.data.remote.APIService
import com.example.app_retrofit.data.remote.ApiUtils
import retrofit2.Response

class RetrofitModel(application: Application) : ViewModel() {
    var employee: MutableLiveData<Employee>
    var employeePostLiveData: MutableLiveData<EmployeePost>
    var contactLiveData: MutableLiveData<Contact>
    var mService: APIService? = null

    init {
        employee = MutableLiveData()
        employeePostLiveData = MutableLiveData()
        contactLiveData = MutableLiveData()
        mService = ApiUtils().getAPIService()
    }

    fun getListDataObserver(): MutableLiveData<Employee> {
        return employee
    }

    fun getAll(mDialog: ProgressDialog, context: Context) {
        mService?.getAll()?.enqueue(object : retrofit2.Callback<Employee> {
            override fun onFailure(call: retrofit2.Call<Employee>, t: Throwable) {
                Log.d("MainActivity", "error loading from API")
                employee.postValue(null)
                mDialog?.dismiss()
                Toast.makeText(context, "error loading from API", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: retrofit2.Call<Employee>, response: Response<Employee>) {
                if (response.isSuccessful) {
                    Log.d("MainActivity", "employees loaded from API")
                    employee.postValue(response.body())
                    mDialog?.dismiss()
                } else {
                    Log.d("MainActivity", response.message() + response.code())
                    employee.postValue(null)
                }
            }
        })
    }

    fun insertUpdate(employeePost: EmployeePost, context: Context) {
        mService?.insert(employeePost)
            ?.enqueue(object : retrofit2.Callback<Contact> {
                override fun onFailure(call: retrofit2.Call<Contact>, t: Throwable) {
                    Log.d("MainActivity", "error")
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                    employeePostLiveData.postValue(null)
                }

                override fun onResponse(
                    call: retrofit2.Call<Contact>,
                    response: Response<Contact>
                ) {
                    if (response.isSuccessful) {
                        employeePostLiveData.postValue(employeePost)
                        Log.d("MainActivity", "successfully")
                        Toast.makeText(context, "successfully", Toast.LENGTH_SHORT).show()
                    } else{
                        employeePostLiveData.postValue(null)
                        Log.d("MainActivity", response.message() + response.code())
                    }


                }
            })
    }


    fun delete(contact: Contact, context: Context) {
        var alertDialog = AlertDialog.Builder(context)
        alertDialog.setMessage("Are you sure you want to delete")
        alertDialog.setPositiveButton(
            "OK",
            DialogInterface.OnClickListener { dialogInterface, i ->
                mService?.delete(contact.contactId)
                    ?.enqueue(object : retrofit2.Callback<Unit> {
                        override fun onFailure(call: retrofit2.Call<Unit>, t: Throwable) {
                            Log.d("MainActivity", "error")
                            Toast.makeText(context, "delete error", Toast.LENGTH_SHORT).show()
                            contactLiveData.postValue(null)
                        }

                        override fun onResponse(
                            call: retrofit2.Call<Unit>,
                            response: Response<Unit>
                        ) {
                            if (response.isSuccessful) {
                                Log.d("MainActivity", "delete successfully")
                                contactLiveData.postValue(contact)
                                Toast.makeText(context, "delete successfully", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                contactLiveData.postValue(null)
                                Log.d("MainActivity", response.message() + response.code())
                            }
                        }
                    })

            }
        )
        alertDialog.setNegativeButton(
            "Cancel ",
            DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.cancel()
            })

        alertDialog.show()
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
