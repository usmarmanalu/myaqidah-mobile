package com.dicoding.myaqidahmobile.core.domain.usecase

import com.dicoding.myaqidahmobile.core.domain.model.*
import com.dicoding.myaqidahmobile.core.domain.repository.*

class DoctorIntercator (private val doctorsRepository: IJadwalDoctorsRepository): DoctorUseCase {

    override fun getAllDoctor() = doctorsRepository.getAllJadwalDoctors()

    override fun getFavoriteDoctor() = doctorsRepository.getFavoriteJadwalDoctors()

    override fun setFavoriteDoctor(doctor: JadwalDoctors, state: Boolean) = doctorsRepository.setFavoriteJadwalDoctors(doctor, state)
}