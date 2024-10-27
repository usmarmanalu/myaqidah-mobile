package com.dicoding.myaqidahmobile.ui.onboarding

import android.annotation.*
import android.content.*
import android.os.*
import android.view.*
import androidx.activity.*
import androidx.appcompat.app.*
import com.dicoding.myaqidahmobile.*
import com.dicoding.myaqidahmobile.R
import com.dicoding.myaqidahmobile.ui.login.*
import com.google.firebase.auth.*

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        setupView()

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        Handler(Looper.getMainLooper()).postDelayed({
            if (user != null) {
                // User is logged in, navigate directly to MainActivity
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // User is not logged in, check onboarding status
                val sharedPreferences = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE)
                val onboardingCompleted = sharedPreferences.getBoolean(MainActivity.KEY_ONBOARDING_COMPLETED, false)
                if (onboardingCompleted) {
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    startActivity(Intent(this, OnboardingScreenActivity::class.java))
                }
            }
            finish()
        }, 1000)
    }

    private fun setupView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        supportActionBar?.hide()
    }
}