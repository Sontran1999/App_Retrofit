package com.example.app_retrofit.viewmodel

import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory


import android.graphics.ColorSpace
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app_demo.Adapter.EmployeeListAdapter
import com.example.app_retrofit.activity.NewEmployeeActivity
import com.example.app_retrofit.data.model.Contact
import com.example.app_retrofit.data.model.ContactPost
import com.example.app_retrofit.data.model.Employee
import com.example.app_retrofit.data.model.EmployeePost
import com.example.app_retrofit.data.remote.APIService
import com.example.app_retrofit.data.remote.ApiUtils
import kotlinx.android.synthetic.main.activity_new_employee.*
import retrofit2.Response
import java.io.ByteArrayOutputStream

class RetrofitModel(application: Application) : ViewModel() {
    var employeePostLiveData: MutableLiveData<EmployeePost>
    var listContact: MutableLiveData<List<Contact>>? = null
    var notification: MutableLiveData<String>
    var mService: APIService? = null
    val validateEmail: String = "[a-zA-Z0-9]+@[a-z]+\\.[a-z]+"

    init {
        employeePostLiveData = MutableLiveData()
        listContact = MutableLiveData()
        mService = ApiUtils().getAPIService()
        notification = MutableLiveData()
    }


    fun getAll() {
        val call = mService?.getAll()
//        mDialog?.setButton(
//            "Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
//                call?.cancel()
//            })

        call?.enqueue(object : retrofit2.Callback<Employee> {
            override fun onFailure(call: retrofit2.Call<Employee>, t: Throwable) {
                Log.d("MainActivity", "error loading from API")
                listContact?.postValue(null)
            }

            override fun onResponse(call: retrofit2.Call<Employee>, response: Response<Employee>) {
                if (response.isSuccessful) {
                    Log.d("MainActivity", "employees loaded from API")
                    listContact?.postValue(response.body()?.contacts)
                } else {
                    Log.d("MainActivity", response.message() + response.code())
                    listContact?.postValue(null)
                }
            }
        })
    }

    fun delete(contact: Contact) {
        mService?.delete(contact.contactId)
            ?.enqueue(object : retrofit2.Callback<Unit> {
                override fun onFailure(call: retrofit2.Call<Unit>, t: Throwable) {
                    Log.d("MainActivity", "error")
                    listContact?.postValue(null)
                }

                override fun onResponse(
                    call: retrofit2.Call<Unit>,
                    response: Response<Unit>
                ) {
                    if (response.isSuccessful) {
                        Log.d("MainActivity", "delete successfully")
                        getAll()
                    } else {
                        listContact?.postValue(null)
                        Log.d("MainActivity", response.message() + response.code())
                    }
                }
            })
    }

    fun search(query: String, list: MutableList<Contact>) {
        var listSearch: MutableList<Contact> = mutableListOf()
        if (query != null) {
            list?.forEachIndexed { index, contact ->
                var name = contact.lastName.toString()
                if (query.toUpperCase().equals(name.toUpperCase())) {
                    listSearch.add(contact)
                } else if (query.toLowerCase().equals(name.toLowerCase())) {
                    listSearch.add(contact)
                }
            }
            listContact?.postValue(listSearch)
        }
    }

    fun stringToBitMap(encodedString: String?): Bitmap? {
        return try {
            val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            e.message
            null
        }
    }

    fun bitMapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun sortASCByName() {
        var sort = listContact?.value as MutableList<Contact>
        sort.sortWith(compareBy { it.lastName?.toLowerCase() })
        listContact?.postValue(sort)
    }

    fun sortDESCByName() {
        var sort = listContact?.value as MutableList<Contact>
        sort.sortWith(compareBy { it.lastName?.toLowerCase() })
        listContact?.postValue(sort.reversed())
    }

    fun check(employeePost: EmployeePost) {
        if (employeePost.contact?.lastName.equals("")) {
            notification.postValue("Không được để trống name")
        } else if (employeePost.contact?.email.equals("")) {
            notification.postValue("Không được để trống email")
        } else if (!employeePost.contact?.email?.matches(validateEmail.toRegex())!!) {
            notification.postValue("Email sai")
        }
        else{
            notification.postValue(null)
        }
    }

    fun Create(employeePost: EmployeePost) {
        val call = mService?.insert(employeePost)
//        var mDialog = ProgressDialog(activity)
//        mDialog?.setMessage("Loading Data...")
//        mDialog?.setCancelable(false)
////        mDialog?.setIndeterminate(true);
//        mDialog?.setButton(
//            "Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
//                dialogInterface.cancel()
//                call?.cancel()
//            })
        call?.enqueue(object : retrofit2.Callback<Contact> {
            override fun onFailure(call: retrofit2.Call<Contact>, t: Throwable) {
                Log.d("MainActivity", "save error")
                employeePostLiveData.postValue(null)
            }

            override fun onResponse(
                call: retrofit2.Call<Contact>,
                response: Response<Contact>
            ) {
                if (response.isSuccessful) {
                    Log.d("MainActivity", "save successfully")
                    employeePostLiveData.postValue(employeePost)
                } else {
                    Log.d("MainActivity", response.message() + response.code())
                    employeePostLiveData.postValue(null)
                }
            }
        })
    }

     fun amIConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
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

