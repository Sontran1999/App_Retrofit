package com.example.app_retrofit.activity


import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_demo.Adapter.EmployeeListAdapter
import com.example.app_retrofit.data.model.Contact
import com.example.app_retrofit.data.model.Employee
import com.example.app_retrofit.R
import com.example.app_retrofit.viewmodel.RetrofitModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    var mApdaper: EmployeeListAdapter? = null
    var mDialog: ProgressDialog? = null
    var list: MutableList<Contact>? = null
    val viewModel: RetrofitModel by lazy {
        ViewModelProviders.of(this, RetrofitModel.ViewModelFactory(this.application))
            .get(RetrofitModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setTitle("")
        mApdaper = EmployeeListAdapter(onItemDelete)
        recycler_employee.setHasFixedSize(true)
        recycler_employee.layoutManager = LinearLayoutManager(this)
        recycler_employee.adapter = mApdaper
        btn_new.setOnClickListener(this)
        replaceData()
    }

    override fun onResume() {
        super.onResume()
        loadFeed()
    }

    private val onItemDelete: (Contact) -> Unit = {
        viewModel.contactLiveData.observe(this, Observer<Contact> {
            if (it != null) {
                load()
            }
        })
        viewModel.delete(it, this)
    }

    fun replaceData() {
        viewModel.employee.observe(this, Observer<Employee> {
            list = it.contacts as MutableList<Contact>
            mApdaper?.setList(list!!)

        })
    }

    fun load() {
        mDialog?.let { viewModel.getAll(it, this) }

    }

    private fun loadFeed() {
        mDialog = ProgressDialog(this)
        mDialog?.setMessage("Loading Data...")
        mDialog?.setCancelable(false)
//        mDialog?.setIndeterminate(true);
        mDialog?.setButton(
            "Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.cancel()
            })
        mDialog?.show()
        if (amIConnected()) {
            mApdaper?.reset()
            load()
        } else {
            Toast.makeText(this, "Thiết bị chưa kết nối internet", Toast.LENGTH_SHORT).show()
            mDialog?.dismiss()
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
                intent.putExtra("key1", 1)
                startActivity(intent)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_refresh -> {
                loadFeed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        var searchItem = menu?.findItem(R.id.menu_search)
        var searchView = searchItem?.actionView as androidx.appcompat.widget.SearchView
        searchView.queryHint = "Enter name"
        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    list?.let {
                        mApdaper?.let { it1 ->
                            viewModel.search(
                                query, it, this@MainActivity,it1
                            )
                        }
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

}