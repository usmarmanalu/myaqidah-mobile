package com.dicoding.myaqidahmobile.ui.home.sectionMenu

import android.graphics.drawable.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.appcompat.app.*
import androidx.core.content.*
import com.bumptech.glide.*
import com.dicoding.myaqidahmobile.R
import com.dicoding.myaqidahmobile.core.helper.*
import com.dicoding.myaqidahmobile.core.utils.DrawableUtils.getWhiteBackArrowDrawable
import com.dicoding.myaqidahmobile.databinding.*

class HakKewajibanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHakKewajibanBinding
    private lateinit var firebaseDataManager: FirebaseDataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHakKewajibanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple2)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Hak dan Kewajiban Pasien"
            setHomeAsUpIndicator(getWhiteBackArrowDrawable(this@HakKewajibanActivity))
            setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this@HakKewajibanActivity,
                        R.color.purple2
                    )
                )
            )
        }

        firebaseDataManager = FirebaseDataManager()

        fetchHakKewajiban()

    }

    private fun fetchHakKewajiban() {
        firebaseDataManager.getHakKewajiban(
            onSuccess = { hak, kewajiban ->

                Glide.with(this)
                    .load(hak)
                    .into(binding.ivHakImage)

                Glide.with(this)
                    .load(kewajiban)
                    .into(binding.ivKewajibanImage)
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