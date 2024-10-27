package com.dicoding.myaqidahmobile

import android.annotation.*
import android.os.*
import android.widget.*
import androidx.activity.*
import androidx.appcompat.app.*
import androidx.core.content.*
import androidx.core.view.*
import androidx.navigation.*
import androidx.navigation.ui.*
import com.dicoding.myaqidahmobile.databinding.*
import com.google.android.material.bottomnavigation.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var doubleBackToExitPressedOnce = false

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple2)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        setSupportActionBar(binding.toolbar)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_pelayanan,
                R.id.navigation_registrasi,
                R.id.navigation_jadwal,
                R.id.navigation_profile
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {

                R.id.navigation_home -> {
                    supportActionBar?.apply {
                        setDisplayShowTitleEnabled(false)
                        setDisplayShowCustomEnabled(true)
                        val customView =
                            layoutInflater.inflate(R.layout.custom_action_bar_logo, null)
                        setCustomView(customView)
                    }
                }

                R.id.navigation_jadwal -> {
                    supportActionBar?.apply {
                        setDisplayShowCustomEnabled(false)
                        setDisplayShowTitleEnabled(true)
                        title = "Jadwal Dokter Spesialis"
                    }
                }

                else -> {
                    supportActionBar?.apply {
                        setDisplayShowCustomEnabled(false)
                        setDisplayShowTitleEnabled(true)
                        when (destination.id) {
                            R.id.navigation_pelayanan -> title = "Pelayanan"
                            R.id.navigation_registrasi -> title = "Registrasi"
                            R.id.navigation_profile -> title = "Profile"
                        }
                    }
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            @Suppress("NAME_SHADOWING")
            override fun handleOnBackPressed() {
                val navController = findNavController(R.id.nav_host_fragment_activity_main)
                if (navController.currentDestination?.id == R.id.navigation_home
                ) {
                    if (doubleBackToExitPressedOnce) {
                        finishAffinity()
                    } else {
                        doubleBackToExitPressedOnce = true
                        Toast.makeText(
                            this@MainActivity,
                            "Tekan sekali lagi untuk keluar",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    companion object {
        const val PREFS_NAME = "MainGeneratePrefs"
        const val KEY_ONBOARDING_COMPLETED = "onboardingCompleted"
    }
}
