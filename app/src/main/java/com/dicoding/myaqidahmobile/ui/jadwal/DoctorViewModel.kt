package com.dicoding.myaqidahmobile.ui.jadwal

import androidx.lifecycle.*
import com.dicoding.myaqidahmobile.core.data.*
import com.dicoding.myaqidahmobile.core.domain.model.*
import com.dicoding.myaqidahmobile.core.domain.usecase.*
import kotlinx.coroutines.flow.*

class DoctorViewModel(private val doctorUseCase: DoctorUseCase) : ViewModel() {
    val getDoctors = doctorUseCase.getAllDoctor().asLiveData()
    fun setFavoriteDoctor(jadwalDoctors: JadwalDoctors, newStatus: Boolean) =
        doctorUseCase.setFavoriteDoctor(jadwalDoctors, newStatus)

}