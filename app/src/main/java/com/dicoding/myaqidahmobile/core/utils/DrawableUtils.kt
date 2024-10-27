package com.dicoding.myaqidahmobile.core.utils

import android.content.*
import android.graphics.drawable.*
import androidx.core.content.*
import androidx.core.graphics.drawable.*

object DrawableUtils {

    fun getWhiteBackArrowDrawable(context: Context): Drawable? {
        val drawable =
            ContextCompat.getDrawable(context, androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        drawable?.let {
            DrawableCompat.setTint(it, ContextCompat.getColor(context, android.R.color.white))
        }
        return drawable
    }
}