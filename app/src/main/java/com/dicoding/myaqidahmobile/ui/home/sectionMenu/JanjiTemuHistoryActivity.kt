package com.dicoding.myaqidahmobile.ui.home.sectionMenu

import android.graphics.drawable.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.activity.*
import androidx.appcompat.app.*
import androidx.core.content.*
import androidx.databinding.DataBindingUtil.setContentView
import androidx.recyclerview.widget.*
import com.dicoding.myaqidahmobile.*
import com.dicoding.myaqidahmobile.R
import com.dicoding.myaqidahmobile.core.firebasemodel.*
import com.dicoding.myaqidahmobile.core.helper.*
import com.dicoding.myaqidahmobile.core.ui.*
import com.dicoding.myaqidahmobile.core.utils.DrawableUtils.getWhiteBackArrowDrawable
import com.dicoding.myaqidahmobile.databinding.*

class JanjiTemuHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJanjiTemuHistoryBinding
    private lateinit var firestore: FirebaseRegistrasiOnlineHelper
    private lateinit var adapter: JanjiTemuHistoryAdapter
    private val janjiTemuList = mutableListOf<JanjiTemuDokter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityJanjiTemuHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseRegistrasiOnlineHelper()

        setupActionBar()
        setupRecyclerView()

        // Menampilkan ProgressBar sebelum data dimuat
        showProgressBar(true)

        fetchData()
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Riwayat Janji Temu"
            setHomeAsUpIndicator(getWhiteBackArrowDrawable(this@JanjiTemuHistoryActivity))
            setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this@JanjiTemuHistoryActivity,
                        R.color.purple2
                    )
                )
            )
        }
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple2)
    }

    private fun setupRecyclerView() {
        adapter = JanjiTemuHistoryAdapter(this, janjiTemuList)
        binding.rvJanjiTemuHistory.apply {
            layoutManager = LinearLayoutManager(this@JanjiTemuHistoryActivity)
            adapter = this@JanjiTemuHistoryActivity.adapter
        }
    }

    private fun fetchData() {
        firestore.fetchDataJanjiTemuHistory(
            onSuccess = { janjiTemu ->
                janjiTemuList.clear()
                janjiTemuList.addAll(janjiTemu)
                adapter.notifyDataSetChanged()

                // Menyembunyikan ProgressBar dan menampilkan RecyclerView
                showProgressBar(false)

                // Menampilkan TextView Empty jika tidak ada data
                if (janjiTemuList.isEmpty()) {
                    binding.tvEmptyState.visibility = View.VISIBLE
                } else {
                    binding.tvEmptyState.visibility = View.GONE
                }
            },
            onFailure = { exception ->
                Toast.makeText(
                    this@JanjiTemuHistoryActivity,
                    "Error: ${exception.message}",
                    Toast.LENGTH_LONG
                ).show()

                // Menyembunyikan ProgressBar jika terjadi error
                showProgressBar(false)

                // Menampilkan TextView Empty jika gagal mendapatkan data
                binding.tvEmptyState.visibility = View.VISIBLE
            }
        )
    }

    private fun showProgressBar(show: Boolean) {
        if (show) {
            binding.progressBar.visibility = View.VISIBLE
            binding.rvJanjiTemuHistory.visibility = View.GONE
            binding.tvEmptyState.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.rvJanjiTemuHistory.visibility = View.VISIBLE
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
