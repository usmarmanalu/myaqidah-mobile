package com.dicoding.myaqidahmobile.core.di

import androidx.room.*
import com.dicoding.myaqidahmobile.core.data.*
import com.dicoding.myaqidahmobile.core.data.source.local.*
import com.dicoding.myaqidahmobile.core.data.source.local.room.*
import com.dicoding.myaqidahmobile.core.data.source.remote.*
import com.dicoding.myaqidahmobile.core.data.source.remote.network.*
import com.dicoding.myaqidahmobile.core.domain.repository.*
import com.dicoding.myaqidahmobile.core.utils.*
import okhttp3.*
import okhttp3.logging.*
import org.koin.android.ext.koin.*
import org.koin.dsl.*
import retrofit2.*
import retrofit2.converter.gson.*
import java.util.concurrent.*

val databaseModule = module {
    factory { get<JadwalDoctorDatabase>().jadwalDoctorDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            JadwalDoctorDatabase::class.java, "JadwalDoctor.db"
        ).fallbackToDestructiveMigration().build()
    }
}

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.192.170:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    factory { AppExecutors() }
    single<IJadwalDoctorsRepository> { AqidahRepository(get(), get(), get()) }
}