package com.dicoding.myaqidahmobile.ui.registrasi

import androidx.lifecycle.*
import com.dicoding.myaqidahmobile.BuildConfig
import com.dicoding.myaqidahmobile.core.data.source.remote.network.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


@FlowPreview
@ExperimentalCoroutinesApi
class PlaceViewModel : ViewModel() {
    private val accessToken = BuildConfig.BASE_TOKEN
    val queryChannel = MutableStateFlow("")

    val searchResult = queryChannel
        .debounce(300)
        .distinctUntilChanged()
        .filter {
            it.trim().isNotEmpty()
        }
        .mapLatest {
            ApiConfig.provideApiService().getCountry(it, accessToken).features
        }
        .asLiveData()
}