package com.dicoding.myaqidahmobile.ui.service.navigation

import android.graphics.drawable.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.activity.*
import androidx.appcompat.app.*
import androidx.core.content.*
import com.bumptech.glide.*
import com.dicoding.myaqidahmobile.R
import com.dicoding.myaqidahmobile.core.helper.*
import com.dicoding.myaqidahmobile.core.utils.DrawableUtils.getWhiteBackArrowDrawable
import com.dicoding.myaqidahmobile.databinding.*

class RawatInapActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRawatInapBinding

    private lateinit var firebaseDataManager: FirebaseDataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRawatInapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDataManager = FirebaseDataManager()

        fetchRawatInap()

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple2)

        // Setup action bar
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Rawat Inap"
            setHomeAsUpIndicator(getWhiteBackArrowDrawable(this@RawatInapActivity))
            setBackgroundDrawable(
                ColorDrawable(ContextCompat.getColor(this@RawatInapActivity, R.color.purple2))
            )
        }
    }

    private fun fetchRawatInap() {
        firebaseDataManager.getRawatInap(
            onSuccess = { image ->
                Glide.with(this)
                    .load(image)
                    .into(binding.ivRawatInap)
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