package com.dicoding.myaqidahmobile.ui.signup.custom

import android.content.*
import android.graphics.*
import android.graphics.drawable.*
import android.util.*
import android.view.*
import androidx.appcompat.widget.*
import androidx.core.content.*
import com.dicoding.myaqidahmobile.*

class BtnRegistration : AppCompatButton {

    private lateinit var enabledBackground: Drawable
    private lateinit var disabledBackground: Drawable

    private var txtColor: Int = 0

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
        background = if (isEnabled) enabledBackground else disabledBackground
        setTextColor(txtColor)
        textSize = 12f
        gravity = Gravity.CENTER
        text = if (isEnabled) "Daftar Sekarang" else "Daftar"
    }

    private fun init() {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        enabledBackground =
            ContextCompat.getDrawable(context, R.drawable.bg_btn_registrasi_enabled) as Drawable
        disabledBackground =
            ContextCompat.getDrawable(context, R.drawable.bg_btn_registrasi_disabled) as Drawable
    }
}