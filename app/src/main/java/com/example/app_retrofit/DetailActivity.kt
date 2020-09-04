package com.example.app_retrofit

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import com.example.app_retrofit.Data.Model.Contact
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        showView()
    }
    fun showView(){

        var bundle = intent.getBundleExtra("data")
        if (bundle != null) {
            var contact = bundle.getSerializable("object") as Contact
            tv_id.setText(contact.contactId)
            tv_lname.setText(contact.lastName)
            tv_email.setText(contact.email)
            tv_create.setText(contact.createdAt)
            tv_update.setText(contact.updatedAt)
            tv_note.setText(contact.customFields?.get(1)?.value)
            img_Picture.setImageBitmap(stringToBitMap(contact.customFields?.get(0)?.value))
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
}