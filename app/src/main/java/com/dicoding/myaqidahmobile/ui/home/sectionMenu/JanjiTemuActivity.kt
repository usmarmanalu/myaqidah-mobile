package com.dicoding.myaqidahmobile.ui.home.sectionMenu

import android.graphics.drawable.*
import android.os.Bundle
import android.view.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.*
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.myaqidahmobile.R
import com.dicoding.myaqidahmobile.core.utils.DrawableUtils.getWhiteBackArrowDrawable
import com.dicoding.myaqidahmobile.databinding.ActivityJanjiTemuBinding

class JanjiTemuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJanjiTemuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityJanjiTemuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple2)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Janji Temu Dokter"
            setHomeAsUpIndicator(getWhiteBackArrowDrawable(this@JanjiTemuActivity))
            setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this@JanjiTemuActivity,
                        R.color.purple2
                    )
                )
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                @Suppress("DEPRECATION")
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}