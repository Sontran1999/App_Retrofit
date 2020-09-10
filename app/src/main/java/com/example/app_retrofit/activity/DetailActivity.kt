package com.example.app_retrofit.activity

import android.database.DatabaseUtils
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.app_retrofit.data.model.Contact
import com.example.app_retrofit.R
import com.example.app_retrofit.databinding.ActivityDetailBinding
import com.example.app_retrofit.viewmodel.RetrofitModel
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    val viewModel: RetrofitModel by lazy {
        ViewModelProviders.of(this, RetrofitModel.ViewModelFactory(this.application))
            .get(RetrofitModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        var bundle = intent.getBundleExtra("data")
        if (bundle != null) {
            var contact = bundle.getSerializable("object") as Contact
            binding.contact = contact
            img_Picture.setImageBitmap(viewModel.stringToBitMap(contact.customFields?.get(0)?.value))
        }

    }

}