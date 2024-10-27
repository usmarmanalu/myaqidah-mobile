package com.dicoding.myaqidahmobile.core.data.source.local.room

import androidx.room.*
import com.dicoding.myaqidahmobile.core.data.source.local.entity.*

@Database(entities = [JadwalDoctorEntity::class], version = 1, exportSchema = false)
abstract class JadwalDoctorDatabase : RoomDatabase() {

    abstract fun jadwalDoctorDao(): JadwalDoctorDao

}