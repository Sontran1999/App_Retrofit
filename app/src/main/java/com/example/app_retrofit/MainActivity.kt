package com.example.app_retrofit


import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_demo.Adapter.EmployeeListAdapter
import com.example.app_retrofit.Data.Model.Data
import com.example.app_retrofit.Data.Model.Employee
import com.example.app_retrofit.Data.Remote.APIService
import com.example.app_retrofit.Data.Remote.ApiUtils
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Response


class MainActivity : AppCompatActivity(), View.OnClickListener {
    var mService: APIService? = null
    var mApdaper: EmployeeListAdapter? = null
    var list: ArrayList<Data> = ArrayList()
    var mDialog: AlertDialog?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setTitle("")

        mService = ApiUtils().getAPIService()
        mApdaper = EmployeeListAdapter()
        mApdaper?.setList(list)
        recycler_employee.setHasFixedSize(true)
        recycler_employee.layoutManager = LinearLayoutManager(this)
        recycler_employee.adapter = mApdaper
        btn_new.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        loadFeed()
    }

    fun load() {
        mService?.getAll()?.enqueue(object : retrofit2.Callback<Employee> {
            override fun onFailure(call: retrofit2.Call<Employee>, t: Throwable) {
                Log.d("MainActivity", "error loading from API");
                mDialog?.dismiss()
            }

            override fun onResponse(call: retrofit2.Call<Employee>, response: Response<Employee>) {
                if (response.isSuccessful) {
                    mApdaper?.setList(response.body()?.data as MutableList<Data>)
                    Log.d("MainActivity", "posts loaded from API")
                    mDialog?.dismiss()
                }
            }
        })

    }

    private fun loadFeed() {
        mDialog = AlertDialog.Builder(this).setMessage("Loading Data...").setCancelable(true).show()
        mApdaper?.reset()
        if (amIConnected()) {
            load()

        } else {
            Toast.makeText(this, "Thiết bị chưa kết nối internet", Toast.LENGTH_SHORT).show()
        }

    }


    private fun amIConnected(): Boolean {
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

//    fun check() {
//        val ret: Boolean = amIConnected()
//        val msg: String
//        msg = if (ret == true) {
//            "Thiết bị đã kết nối internet"
//        } else {
//            "Thiết bị chưa kết nối internet"
//        }
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
//    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_new -> {
                val intent: Intent = Intent(this, NewEmployeeActivity::class.java)
                intent.putExtra("key1", 0)
                startActivity(intent)
            }
        }
    }
}