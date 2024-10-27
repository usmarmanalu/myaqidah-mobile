package com.dicoding.myaqidahmobile.core.domain.usecase

import com.dicoding.myaqidahmobile.core.data.*
import com.dicoding.myaqidahmobile.core.domain.model.*
import kotlinx.coroutines.flow.*

interface DoctorUseCase {
    fun getAllDoctor(): Flow<Resource<List<JadwalDoctors>>>
    fun getFavoriteDoctor(): Flow<List<JadwalDoctors>>
    fun setFavoriteDoctor(doctor: JadwalDoctors, state: Boolean)
}