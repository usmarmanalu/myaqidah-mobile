package com.dicoding.myaqidahmobile.ui.home.sectionMenu

import android.annotation.*
import android.graphics.drawable.*
import android.os.Bundle
import android.view.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.*
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.*
import com.dicoding.myaqidahmobile.R
import com.dicoding.myaqidahmobile.core.firebasemodel.*
import com.dicoding.myaqidahmobile.core.helper.*
import com.dicoding.myaqidahmobile.core.ui.*
import com.dicoding.myaqidahmobile.core.utils.DrawableUtils.getWhiteBackArrowDrawable
import com.dicoding.myaqidahmobile.databinding.*
import com.google.android.material.snackbar.*

class InfoKesehatanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoKesehatanBinding

    private lateinit var artikelAdapter: ArtikelAdapter
    private lateinit var firebaseDataManager: FirebaseDataManager
    private val listArtikel = ArrayList<Artikel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityInfoKesehatanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDataManager = FirebaseDataManager()

        setupRecyclerView()

        loadArticles()

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple2)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Info Kesehatan"
            setHomeAsUpIndicator(getWhiteBackArrowDrawable(this@InfoKesehatanActivity))
            setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this@InfoKesehatanActivity,
                        R.color.purple2
                    )
                )
            )
        }
    }

    private fun setupRecyclerView() {
        artikelAdapter = ArtikelAdapter(listArtikel)
        binding.rvArtikel.apply {
            layoutManager = LinearLayoutManager(this@InfoKesehatanActivity)
            adapter = artikelAdapter
            setHasFixedSize(true)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadArticles() {
        firebaseDataManager.getArticles(
            onSuccess = { articles ->
                listArtikel.clear()
                listArtikel.addAll(articles)
                artikelAdapter.notifyDataSetChanged()
            },
            onFailure = { exception ->
                showSnackbar("Failed to load articles: ${exception.message}")
            }
        )
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
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