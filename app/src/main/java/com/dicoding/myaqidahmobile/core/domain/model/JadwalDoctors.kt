package com.dicoding.myaqidahmobile.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.*

@Parcelize
data class JadwalDoctors(
    val id:  Int,
    val image: String,
    val schedule: String,
    val name: String,
    var isFavorite: Boolean,
    val type: String
) : Parcelable