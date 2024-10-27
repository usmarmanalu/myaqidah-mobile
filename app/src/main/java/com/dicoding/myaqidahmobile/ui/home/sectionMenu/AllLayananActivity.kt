package com.dicoding.myaqidahmobile.ui.home.sectionMenu

import android.graphics.drawable.*
import android.os.*
import android.view.*
import androidx.activity.*
import androidx.appcompat.app.*
import androidx.core.content.*
import com.dicoding.myaqidahmobile.R
import com.dicoding.myaqidahmobile.core.utils.DrawableUtils.getWhiteBackArrowDrawable
import com.dicoding.myaqidahmobile.databinding.*

class AllLayananActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllLayananBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAllLayananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple2)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Semua Layanan"
            setHomeAsUpIndicator(getWhiteBackArrowDrawable(this@AllLayananActivity))
            setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this@AllLayananActivity,
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