package com.dicoding.myaqidahmobile.core.data.source.remote.network

import com.dicoding.myaqidahmobile.core.data.source.remote.response.*
import retrofit2.http.*

interface ApiService {
    @GET("/doctors")
    suspend fun getDoctors(): ListJadwalDokterResponse

    @GET("mapbox.places/{query}.json")
    suspend fun getCountry(
        @Path("query") query: String,
        @Query("access_token") accessToken: String,
        @Query("autocomplete") autoComplete: Boolean = true
    ): PlaceResponse
}