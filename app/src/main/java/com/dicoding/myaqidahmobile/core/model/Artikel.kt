package com.dicoding.myaqidahmobile.core.model

import android.os.Parcelable
import kotlinx.parcelize.*

@Parcelize
data class Artikel(
    var image: String = "",       // Default value for image
    var title: String = "",       // Default value for title
    var artikel: String = "",     // Default value for artikel
    var referensi: String = "",   // Default value for referensi
    var uploaded: String = ""
) : Parcelable
