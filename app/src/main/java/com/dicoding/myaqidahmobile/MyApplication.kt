package com.dicoding.myaqidahmobile

import android.app.*
import com.dicoding.myaqidahmobile.core.di.*
import com.dicoding.myaqidahmobile.di.*
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.*
import org.koin.core.context.*
import org.koin.core.logger.*

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }

        FirebaseApp.initializeApp(this)
    }
}