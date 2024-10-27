package com.dicoding.myaqidahmobile.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class DokterResponse(
	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("schedule")
	val schedule: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("type")
	val type: String,

)
