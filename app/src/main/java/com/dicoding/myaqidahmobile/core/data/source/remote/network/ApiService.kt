package com.dicoding.myaqidahmobile.core.data.source.remote.network

import com.dicoding.myaqidahmobile.core.data.source.remote.response.*
import retrofit2.http.*

interface ApiService {
    @GET("/doctors")
    suspend fun getDoctors(): ListJadwalDokterResponse
}