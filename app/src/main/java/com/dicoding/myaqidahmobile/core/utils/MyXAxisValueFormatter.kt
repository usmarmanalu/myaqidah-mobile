package com.dicoding.myaqidahmobile.core.utils

import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.formatter.*
import java.text.*
import java.util.*

class MyXAxisValueFormatter : ValueFormatter() {
    private val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val date = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, value.toInt())
        }.time
        return dateFormat.format(date)
    }
}