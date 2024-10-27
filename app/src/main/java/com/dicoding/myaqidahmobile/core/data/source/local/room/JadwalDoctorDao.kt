package com.dicoding.myaqidahmobile.core.data.source.local.room

import androidx.room.*
import com.dicoding.myaqidahmobile.core.data.source.local.entity.*
import kotlinx.coroutines.flow.*

@Dao
interface JadwalDoctorDao {

    @Query("SELECT * FROM jadwal_doctor")
    fun getAllJadwalDoctor(): Flow<List<JadwalDoctorEntity>>

    @Query("SELECT * FROM jadwal_doctor where isFavorite  = 1")
    fun getFavoriteJadwalDoctor(): Flow<List<JadwalDoctorEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJadwalDoctor(doctor: List<JadwalDoctorEntity>)

    @Update
    fun updateFavoriteJadwalDoctorv(doctor: JadwalDoctorEntity)
}