package com.dicoding.myaqidahmobile.core.utils

import android.content.*
import android.graphics.drawable.*
import androidx.core.content.*
import androidx.core.graphics.drawable.*
import com.dicoding.myaqidahmobile.*

object DrawableUtils {

    fun getWhiteBackArrowDrawable(context: Context): Drawable? {
        val drawable =
            ContextCompat.getDrawable(context, R.drawable.baseline_navigate_before_24)
        drawable?.let {
            DrawableCompat.setTint(it, ContextCompat.getColor(context, android.R.color.white))
        }
        return drawable
    }
}
