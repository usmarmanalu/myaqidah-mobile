package com.dicoding.myaqidahmobile.ui.service.navigation

import android.graphics.drawable.*
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.*
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.*
import com.dicoding.myaqidahmobile.R
import com.dicoding.myaqidahmobile.core.helper.*
import com.dicoding.myaqidahmobile.core.utils.DrawableUtils.getWhiteBackArrowDrawable
import com.dicoding.myaqidahmobile.databinding.*

class RawatJalanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRawatJalanBinding

    private lateinit var firebaseDataManager: FirebaseDataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRawatJalanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDataManager = FirebaseDataManager()

        fetchRawatJalan()

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple2)

        // Setup action bar
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Rawat Jalan"
            setHomeAsUpIndicator(getWhiteBackArrowDrawable(this@RawatJalanActivity))
            setBackgroundDrawable(
                ColorDrawable(ContextCompat.getColor(this@RawatJalanActivity, R.color.purple2))
            )
        }
    }

    private fun fetchRawatJalan() {
        firebaseDataManager.getRawatJalan(
            onSuccess = { image ->
                Glide.with(this)
                    .load(image)
                    .into(binding.ivRawatJalan)
            },
            onFailure = { exception ->
                // Handle error (e.g., display an error message)
                Toast.makeText(
                    this,
                    "Failed to load information: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
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