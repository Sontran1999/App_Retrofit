package com.example.app_retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.example.app_demo.Adapter.EmployeeListAdapter
import com.example.app_retrofit.Data.Model.Data
import com.example.app_retrofit.Data.Model.Employee
import com.example.app_retrofit.Data.Remote.APIService
import com.example.app_retrofit.Data.Remote.ApiUtils
import kotlinx.android.synthetic.main.activity_new_employee.*
import kotlinx.android.synthetic.main.employee_item.*
import retrofit2.Response

class NewEmployeeActivity : AppCompatActivity(), View.OnClickListener {
    var mService: APIService? = null
    var mApdaper: EmployeeListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_employee)
        supportActionBar?.setTitle("")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mService = ApiUtils().getAPIService()
        btn_save.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_save -> {
                save()
            }
        }
    }

    fun save() {
        mService?.insert(
            edt_id.text.toString().toInt(),
            edt_name.text.toString(),
            edt_salary.text.toString().toInt(),
            edt_age.text.toString().toInt(),
            ""
        )?.enqueue(object : retrofit2.Callback<Data> {
            override fun onFailure(call: retrofit2.Call<Data>, t: Throwable) {
                Log.d("MainActivity", "error");
            }

            override fun onResponse(call: retrofit2.Call<Data>, response: Response<Data>) {
                if (response.isSuccessful) {
                    Log.d("MainActivity", "success")

                }
            }
        })
        finish()
    }
}