package com.dicoding.myaqidahmobile.core.domain.repository

import com.dicoding.myaqidahmobile.core.data.*
import com.dicoding.myaqidahmobile.core.domain.model.*
import kotlinx.coroutines.flow.*

interface IJadwalDoctorsRepository {

    fun getAllJadwalDoctors(): Flow<Resource<List<JadwalDoctors>>>

    fun getFavoriteJadwalDoctors(): Flow<List<JadwalDoctors>>

    fun setFavoriteJadwalDoctors(tourism: JadwalDoctors, state: Boolean)
}