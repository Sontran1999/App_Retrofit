package com.example.app_retrofit.activity

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.app_retrofit.data.model.Contact
import com.example.app_retrofit.data.model.ContactPost
import com.example.app_retrofit.data.model.CustomPost
import com.example.app_retrofit.data.model.EmployeePost
import com.example.app_retrofit.data.remote.APIService
import com.example.app_retrofit.data.remote.ApiUtils
import com.example.app_retrofit.R
import com.example.app_retrofit.viewmodel.RetrofitModel
import kotlinx.android.synthetic.main.activity_new_employee.*
import java.io.ByteArrayOutputStream
import java.io.IOException


class NewEmployeeActivity : AppCompatActivity(), View.OnClickListener {
    var mService: APIService? = null
    val validateEmail: String = "[a-zA-Z0-9]+@[a-z]+\\.[a-z]+"
    val viewModel: RetrofitModel by lazy {
        ViewModelProviders.of(this, RetrofitModel.ViewModelFactory(this.application))
            .get(RetrofitModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_employee)
        supportActionBar?.setTitle("")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mService = ApiUtils().getAPIService()
        btn_save.setOnClickListener(this)
        showView()
        btnChoose.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showView() {
        var bundle = intent.getBundleExtra("data")
        if (bundle != null) {
            var key2: Int? = bundle.getInt("key2")
            if (key2 == 2) {
                var contact = bundle.getSerializable("object") as Contact
                edt_lname.setText(contact.lastName)
                edt_email.setText(contact.email)
                edt_email.isFocusable = false
                contact.customFields?.forEachIndexed { index, customField ->
                    edt_note.setText(customField.value.toString())
                }
                imgPicture.setImageBitmap(stringToBitMap(contact.customFields?.get(0)?.value))
            }
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

    private fun choosePicture() {
        val pickPhoto = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(pickPhoto, 200)
    }//one can be replaced with any action code

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == RESULT_OK) {
            try {
                //xử lý lấy ảnh chọn từ điện thoại:
                val imageUri = data?.data
                var selectedBitmap =
                    MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                imgPicture.setImageBitmap(selectedBitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_save -> {
                save()
            }
            R.id.btnChoose -> {
                choosePicture()
            }
        }
    }
    fun save() {
        var lname = edt_lname.text.toString()
        var email = edt_email.text.toString()
        var note = edt_note.text.toString()
        var image = bitMapToString((imgPicture.drawable as BitmapDrawable).bitmap)
         if (lname.equals("")) {
            Toast.makeText(this, "Không được để trống last name", Toast.LENGTH_SHORT).show()
        } else if (email.equals("")) {
            Toast.makeText(this, "Không được để trống email", Toast.LENGTH_SHORT).show()
        } else if (!email.matches(validateEmail.toRegex())) {
            edt_email.error = "Email sai"
        } else {
            var contact = ContactPost(lname, email, CustomPost(image, note))
            var employeePost = EmployeePost(contact)
            var bundle = intent.getBundleExtra("data")
            if (bundle != null) {
                var alertDialog = AlertDialog.Builder(this)
                alertDialog.setMessage("Are you sure you want to update information")
                alertDialog.setPositiveButton(
                    "OK",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        update(employeePost)
                    }
                )
                alertDialog.setNegativeButton(
                    "Cancel ",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.cancel()
                    })

                alertDialog.show()

            } else {
                insert(employeePost)
            }
        }
    }


    fun update(employeePost: EmployeePost) {
        viewModel.employeePostLiveData.observe(this, Observer<EmployeePost> {
            if (it != null) {
                Log.d("MainActivity", "update successfully")
                Toast.makeText(
                    this@NewEmployeeActivity,
                    "update successfully",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        })
        viewModel.insertUpdate(employeePost)
    }

    fun insert(employeePost: EmployeePost) {
        viewModel.employeePostLiveData.observe(this, Observer<EmployeePost> {
            if (it != null) {
                Log.d("MainActivity", "save successfully")
                Toast.makeText(
                    this@NewEmployeeActivity,
                    "save successfully",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
            else{
                Toast.makeText(
                    this@NewEmployeeActivity,
                    "save error",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        viewModel.insertUpdate(employeePost)
    }


    fun bitMapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
}
