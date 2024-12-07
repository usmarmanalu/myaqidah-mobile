package com.dicoding.myaqidahmobile.core.data.source.remote.response

import com.google.gson.annotations.*

data class PlacesItem(
    @field:SerializedName("place_name")
    val placeName: String
)