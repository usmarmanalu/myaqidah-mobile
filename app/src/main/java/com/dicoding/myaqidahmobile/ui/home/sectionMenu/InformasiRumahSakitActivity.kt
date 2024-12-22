package com.dicoding.myaqidahmobile.ui.home.sectionMenu

import android.graphics.drawable.*
import android.os.*
import android.view.*
import androidx.appcompat.app.*
import androidx.core.content.*
import com.bumptech.glide.*
import com.dicoding.myaqidahmobile.R
import com.dicoding.myaqidahmobile.core.helper.*
import com.dicoding.myaqidahmobile.core.utils.DrawableUtils.getWhiteBackArrowDrawable
import com.dicoding.myaqidahmobile.databinding.*

class InformasiRumahSakitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInformasiRumahSakitBinding

    private lateinit var firebaseDataManager: FirebaseDataManager
    private var isHistoryExpanded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformasiRumahSakitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Informasi Rumah Sakit"
            setHomeAsUpIndicator(getWhiteBackArrowDrawable(this@InformasiRumahSakitActivity))
            setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this@InformasiRumahSakitActivity,
                        R.color.purple2
                    )
                )
            )
        }

        firebaseDataManager = FirebaseDataManager()

        fetchHospitalInformation()

        binding.seeMore.setOnClickListener {
            isHistoryExpanded = !isHistoryExpanded
            updateHistoryContent()
        }
    }

    private fun fetchHospitalInformation() {
        firebaseDataManager.getHospitalInformation(
            onSuccess = { sejarah, visi, misi, motto, imageUrl ->
                binding.tvHistoryContent.text = sejarah
                binding.tvVisionContent.text = visi
                binding.tvMissionContent.text = misi
                binding.tvMottoContent.text = motto

                Glide.with(this)
                    .load(imageUrl)
                    .into(binding.ivHospitalImage)
            },
            onFailure = { exception ->
                // Handle error (e.g., display an error message)
                binding.tvHistoryContent.text =
                    getString(R.string.failed_to_load_information, exception.message)
            }
        )
    }

    private fun updateHistoryContent() {
        if (isHistoryExpanded) {
            binding.tvHistoryContent.maxLines = Integer.MAX_VALUE
            binding.seeMore.text = getString(R.string.lihat_lebih_sedikit)
        } else {
            binding.tvHistoryContent.maxLines = 5 // Collapse to 5 lines
            binding.seeMore.text = getString(R.string.lihat_selengkapnya)
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
