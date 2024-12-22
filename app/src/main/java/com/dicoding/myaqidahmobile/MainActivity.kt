package com.dicoding.myaqidahmobile

import android.annotation.*
import android.content.pm.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.activity.*
import androidx.appcompat.app.*
import androidx.core.app.*
import androidx.core.content.*
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

        window.statusBarColor = ContextCompat.getColor(this, R.color.blue_soft)

        setSupportActionBar(binding.toolbar)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_pelayanan,
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
                            getString(R.string.tekan_sekali_lagi_untuk_keluar),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })

        // Periksa izin POST_NOTIFICATIONS saat pertama kali aplikasi dibuka
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_CODE_POST_NOTIFICATIONS
                )
            }
        }
    }

    companion object {
        const val PREFS_NAME = "MainGeneratePrefs"
        const val KEY_ONBOARDING_COMPLETED = "onboardingCompleted"
        const val REQUEST_CODE_POST_NOTIFICATIONS = 101
    }
}