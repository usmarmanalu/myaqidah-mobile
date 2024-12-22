package com.dicoding.myaqidahmobile.ui.utils

import com.dicoding.myaqidahmobile.core.data.source.local.entity.*
import com.dicoding.myaqidahmobile.core.domain.model.*

object DataDummy {
    fun generateDummyDoctorEntity(): List<JadwalDoctors> {
        val doctorList = ArrayList<JadwalDoctors>()
        for(i in 0..10) {
            val doctor = JadwalDoctors(
                1,
                "https://rsaqidah.com/assets/img/dokter/Ulil-SpAn1.jpg",
                "Rabu: 14:00 - 17:00 Jumat: 14:00 - 17:00",
                "dr. BELLYA AFFAN ROES, Sp.PK. (K), MMRS.",
                "Dokter Spesialis Patologi Klinik",
                true
            )
            doctorList.add(doctor)
        }
        return doctorList
    }
}