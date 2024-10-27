package com.dicoding.myaqidahmobile.core.data.source.local.entity

import androidx.room.*

@Entity(tableName = "jadwal_doctor")
data class JadwalDoctorEntity(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "image")
    val image: String,

    @ColumnInfo(name = "schedule")
    val schedule: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false

)
