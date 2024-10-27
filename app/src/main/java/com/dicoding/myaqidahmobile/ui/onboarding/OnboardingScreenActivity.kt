package com.dicoding.myaqidahmobile.ui.onboarding

import android.content.*
import android.os.*
import androidx.activity.*
import androidx.appcompat.app.*
import com.dicoding.myaqidahmobile.*
import com.dicoding.myaqidahmobile.databinding.*
import com.dicoding.myaqidahmobile.ui.login.*

class OnboardingScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOnboardingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val viewPager = binding.viewPager
        val indicator = binding.indicator
        val skipButton = binding.skipButton
        val nextButton = binding.nextButton

        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        // Set up the indicator with the ViewPager2
        indicator.attachTo(viewPager)

        // Handle button clicks
        skipButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        nextButton.setOnClickListener {
            if (viewPager.currentItem < adapter.itemCount - 1) {
                viewPager.currentItem += 1
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}