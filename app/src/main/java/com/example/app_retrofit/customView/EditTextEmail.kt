package com.example.app_retrofit.customView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import com.example.app_retrofit.R

class EditTextEmail : AppCompatEditText {
    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    val validateEmail: String = "[a-zA-Z0-9]+@[a-z]+\\.[c][o][m]+"
    var mImage: Drawable? = null

    init {

        // Initialize Drawable member variable.
        mImage = ResourcesCompat.getDrawable(
            resources,
            R.drawable.ic_baseline_check_circle_24, null
        )

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int, count: Int, after: Int
            ) {
                // Do nothing.
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int, before: Int, count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.matches(validateEmail.toRegex())) {
                    showImage()
                }
                else{
                    hideImage()

                }
            }
        })
    }

    @SuppressLint("NewApi")
    fun showImage() {
        this.setCompoundDrawablesRelativeWithIntrinsicBounds(
            null, null, mImage, null
        )
    }
    @SuppressLint("NewApi")
    fun hideImage(){
        this.setCompoundDrawablesRelativeWithIntrinsicBounds(
            null, null, null, null
        )
    }
}

