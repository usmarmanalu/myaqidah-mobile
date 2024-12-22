package com.dicoding.myaqidahmobile.core.data

import com.dicoding.myaqidahmobile.core.data.source.local.*
import com.dicoding.myaqidahmobile.core.data.source.remote.*
import com.dicoding.myaqidahmobile.core.data.source.remote.network.*
import com.dicoding.myaqidahmobile.core.data.source.remote.response.*
import com.dicoding.myaqidahmobile.core.domain.model.*
import com.dicoding.myaqidahmobile.core.domain.repository.*
import com.dicoding.myaqidahmobile.core.utils.*
import kotlinx.coroutines.flow.*

class AqidahRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors,
) : IJadwalDoctorsRepository {

    override fun getAllJadwalDoctors(): Flow<Resource<List<JadwalDoctors>>> =
        object : NetworkBoundResource<List<JadwalDoctors>, List<DokterResponse>>() {
            override fun loadFromDB(): Flow<List<JadwalDoctors>> {
                return localDataSource.getAllJadwalDoctor().map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<JadwalDoctors>?): Boolean = data.isNullOrEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<DokterResponse>>> =
                remoteDataSource.getAllJadwalDoctor()

            override suspend fun saveCallResult(data: List<DokterResponse>) {
                val doctorList = DataMapper.mapResponseToEntities(data)
                localDataSource.insertJadwalDoctor(doctorList)
            }
        }.asFlow()

    override fun getFavoriteJadwalDoctors(): Flow<List<JadwalDoctors>> {
        return localDataSource.getFavoriteJadwalDoctor().map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override fun setFavoriteJadwalDoctors(jadwalDoctors: JadwalDoctors, state: Boolean) {
        val jadwalDoctorEntity = DataMapper.mapDomainToEntity(jadwalDoctors)
        appExecutors.diskIO().execute {
            localDataSource
                .setFavoriteJadwalDoctor(jadwalDoctorEntity, state)
        }
    }
}