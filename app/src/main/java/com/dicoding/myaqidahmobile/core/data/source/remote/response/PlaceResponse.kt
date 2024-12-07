package com.dicoding.myaqidahmobile.core.data.source.remote.response

import com.google.gson.annotations.*

data class PlaceResponse(
    @field:SerializedName("features")
    val features: List<PlacesItem>
)