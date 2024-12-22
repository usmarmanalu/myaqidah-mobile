package com.dicoding.myaqidahmobile.ui.home.menu

import android.content.*
import android.graphics.drawable.*
import android.net.*
import android.os.*
import android.view.*
import androidx.appcompat.app.*
import androidx.core.content.*
import com.dicoding.myaqidahmobile.*
import com.dicoding.myaqidahmobile.core.utils.DrawableUtils.getWhiteBackArrowDrawable
import com.dicoding.myaqidahmobile.databinding.*

@Suppress("DEPRECATION")
class EmergencyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmergencyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmergencyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple2)

        // Setup action bar with title and back button
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.gawat_darurat)
            setHomeAsUpIndicator(getWhiteBackArrowDrawable(this@EmergencyActivity))
            setBackgroundDrawable(
                ColorDrawable(ContextCompat.getColor(this@EmergencyActivity, R.color.purple2))
            )
        }

        // Implement emergency call button
        binding.btnCall.setOnClickListener {
            val phoneNumber = "(021) 731 0851"
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            startActivity(intent)
        }

        binding.btnMaps.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}
