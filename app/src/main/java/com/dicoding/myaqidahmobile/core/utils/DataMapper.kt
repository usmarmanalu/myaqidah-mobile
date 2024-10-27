package com.dicoding.myaqidahmobile.core.utils

import com.dicoding.myaqidahmobile.core.data.source.local.entity.*
import com.dicoding.myaqidahmobile.core.data.source.remote.response.*
import com.dicoding.myaqidahmobile.core.domain.model.*

object DataMapper {

    // Map Response to Entities
    fun mapResponseToEntities(input: List<DokterResponse>): List<JadwalDoctorEntity> {
        val jadwalDoctorList = ArrayList<JadwalDoctorEntity>()
        input.map {
            val jadwalDoctor = JadwalDoctorEntity(
                id = it.id,
                image = it.image,
                schedule = it.schedule,
                name = it.name,
                type = it.type,
                isFavorite = false,
            )
            jadwalDoctorList.add(jadwalDoctor)
        }

        return jadwalDoctorList
    }

    // Map Entities to Domain
    fun mapEntitiesToDomain(input: List<JadwalDoctorEntity>): List<JadwalDoctors> =
        input.map {
            JadwalDoctors(
                id = it.id,
                image = it.image,
                schedule = it.schedule,
                name = it.name,
                isFavorite = it.isFavorite,
                type = it.type,
            )
        }

    // Map Domain to Entity
    fun mapDomainToEntity(input: JadwalDoctors) = JadwalDoctorEntity(
        id = input.id,
        image = input.image,
        schedule = input.schedule,
        name = input.name,
        isFavorite = input.isFavorite,
        type = input.type
    )
}
