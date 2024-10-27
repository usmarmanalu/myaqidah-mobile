package com.dicoding.myaqidahmobile.ui.jadwal.favorite

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.myaqidahmobile.R
import com.dicoding.myaqidahmobile.core.ui.DoctorAdapter
import com.dicoding.myaqidahmobile.core.utils.DrawableUtils.getWhiteBackArrowDrawable
import com.dicoding.myaqidahmobile.databinding.ActivityFavoriteBinding
import com.dicoding.myaqidahmobile.ui.jadwal.ScheduleFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val favoriteViewModel: FavoriteViewModel by viewModel()
    private lateinit var doctorAdapter: DoctorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        // Initialize the doctorAdapter here
        doctorAdapter = DoctorAdapter(this)

        // Set onItemClick listener here
        doctorAdapter.onItemClick = { selectedData ->
            val intent = Intent(this, ScheduleFragment::class.java)
            intent.putExtra(ScheduleFragment.EXTRA_DATA, selectedData)
            startActivity(intent)
        }

        binding.rvDoctor.apply {
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            adapter = doctorAdapter
        }

        // Observe favorite doctors
        favoriteViewModel.favoriteDoctors.observe(this) { doctors ->
            doctorAdapter.submitList(doctors)
            binding.viewEmpty.root.visibility =
                if (doctors.isNotEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Dokter Favorit"
            setHomeAsUpIndicator(getWhiteBackArrowDrawable(this@FavoriteActivity))
            setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this@FavoriteActivity,
                        R.color.purple2
                    )
                )
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
