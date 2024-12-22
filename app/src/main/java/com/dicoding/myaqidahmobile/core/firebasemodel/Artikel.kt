package com.dicoding.myaqidahmobile.core.firebasemodel

import android.os.*
import kotlinx.parcelize.*

@Parcelize
data class Artikel(
    var image: String = "",
    var title: String = "",
    var artikel: String = "",
    var referensi: String = "",
    var uploaded: String = ""
) : Parcelable
