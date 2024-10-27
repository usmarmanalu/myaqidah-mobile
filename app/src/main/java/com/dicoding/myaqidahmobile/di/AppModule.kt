package com.dicoding.myaqidahmobile.di

import com.dicoding.myaqidahmobile.core.domain.usecase.*
import com.dicoding.myaqidahmobile.ui.jadwal.*
import com.dicoding.myaqidahmobile.ui.jadwal.favorite.*
import org.koin.core.module.dsl.*
import org.koin.dsl.*

val useCaseModule = module {
    factory<DoctorUseCase> { DoctorIntercator(get()) }
}

val viewModelModule = module {
    viewModel { DoctorViewModel(get()) }
    viewModel { FavoriteViewModel(get()) }

}