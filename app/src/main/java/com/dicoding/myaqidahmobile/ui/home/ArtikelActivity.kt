package com.dicoding.myaqidahmobile.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dicoding.myaqidahmobile.R
import com.dicoding.myaqidahmobile.core.firebasemodel.Artikel
import com.dicoding.myaqidahmobile.core.utils.DrawableUtils.getWhiteBackArrowDrawable
import com.dicoding.myaqidahmobile.databinding.ActivityArtikelBinding

@Suppress("DEPRECATION")
class ArtikelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArtikelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityArtikelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple2)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Detail Artikel"
            setHomeAsUpIndicator(getWhiteBackArrowDrawable(this@ArtikelActivity))
            setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(this@ArtikelActivity, R.color.purple2)
                )
            )
        }

        setupData()
    }

    @SuppressLint("SetTextI18n")
    private fun setupData() {
        val artikel = intent.getParcelableExtra<Artikel>("artikel") as Artikel
        Glide.with(applicationContext)
            .load(artikel.image)
            .circleCrop()
            .into(binding.artikelImageView)

        binding.titleTextView.text = artikel.title
        binding.artikelTextView.text = artikel.artikel
        binding.uploadedTextView.text = "Uploaded: ${artikel.uploaded}"

        // Mengatur referensi atau tampilkan "-"
        if (artikel.referensi.isEmpty()) {
            binding.referensiTextView.text = "-"
            binding.referensiTextView.setTextColor(ContextCompat.getColor(this, R.color.grey))
            binding.referensiTextView.paint.isUnderlineText = false
            binding.referensiTextView.setOnClickListener(null)
        } else {
            binding.referensiTextView.apply {
                text = artikel.referensi
                setTextColor(ContextCompat.getColor(this@ArtikelActivity, R.color.blue))
                paint.isUnderlineText = true

                setOnClickListener {
                    try {
                        val uri = Uri.parse(artikel.referensi)
                        val browserIntent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(browserIntent)
                    } catch (e: Exception) {
                        Toast.makeText(this@ArtikelActivity, "Tautan tidak valid", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
