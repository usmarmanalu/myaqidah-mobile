package com.dicoding.myaqidahmobile.core.data.source.local

import com.dicoding.myaqidahmobile.core.data.source.local.entity.*
import com.dicoding.myaqidahmobile.core.data.source.local.room.*
import kotlinx.coroutines.flow.*

class LocalDataSource(private val jadwalDoctorDao: JadwalDoctorDao) {

    fun getAllJadwalDoctor(): Flow<List<JadwalDoctorEntity>> = jadwalDoctorDao.getAllJadwalDoctor()

    fun getFavoriteJadwalDoctor(): Flow<List<JadwalDoctorEntity>> = jadwalDoctorDao.getFavoriteJadwalDoctor()

    suspend fun insertJadwalDoctor(doctor: List<JadwalDoctorEntity>) = jadwalDoctorDao.insertJadwalDoctor(doctor)

    fun setFavoriteJadwalDoctor(doctor: JadwalDoctorEntity, newState: Boolean) {
        doctor.isFavorite = newState
        jadwalDoctorDao.updateFavoriteJadwalDoctorv(doctor)

    }
}