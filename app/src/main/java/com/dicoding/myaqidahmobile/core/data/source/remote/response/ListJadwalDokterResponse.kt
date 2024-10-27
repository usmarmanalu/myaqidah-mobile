package com.dicoding.myaqidahmobile.core.data.source.remote.response

import com.google.gson.annotations.*

data class ListJadwalDokterResponse (

    @field:SerializedName("jadwaldoctors")
    val jadwaldoctors: List<DokterResponse>,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null

)