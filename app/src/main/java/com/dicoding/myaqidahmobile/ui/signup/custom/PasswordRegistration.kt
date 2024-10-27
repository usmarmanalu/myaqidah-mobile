package com.dicoding.myaqidahmobile.ui.signup.custom

import android.content.*
import android.graphics.*
import android.text.*
import android.util.*
import android.view.*
import androidx.appcompat.widget.*
import com.dicoding.myaqidahmobile.*

class PasswordRegistration : AppCompatEditText, View.OnTouchListener {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Password"
        textSize = 14f
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        setOnTouchListener(this)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePasswordLength(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun validatePasswordLength(password: String) {
        val passwordLength = password.length
        error = if (passwordLength < 6) {
            context.getString(R.string.message_password)
        } else {
            null
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return false
    }
}