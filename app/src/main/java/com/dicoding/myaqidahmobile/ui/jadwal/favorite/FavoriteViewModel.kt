package com.dicoding.myaqidahmobile.ui.jadwal.favorite

import androidx.lifecycle.*
import com.dicoding.myaqidahmobile.core.domain.usecase.*

class FavoriteViewModel(doctorUseCase: DoctorUseCase) : ViewModel() {
    val favoriteDoctors = doctorUseCase.getFavoriteDoctor().asLiveData()

}
