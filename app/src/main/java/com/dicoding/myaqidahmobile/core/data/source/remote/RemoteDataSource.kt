package com.dicoding.myaqidahmobile.core.data.source.remote

import com.dicoding.myaqidahmobile.core.data.source.remote.network.*
import com.dicoding.myaqidahmobile.core.data.source.remote.response.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class RemoteDataSource(private val apiService: ApiService) {

    suspend fun getAllJadwalDoctor(): Flow<ApiResponse<List<DokterResponse>>> {
        //get data from remote api
        return flow {
            try {
                val response = apiService.getDoctors()
                val dataArray = response.jadwaldoctors
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(response.jadwaldoctors))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}