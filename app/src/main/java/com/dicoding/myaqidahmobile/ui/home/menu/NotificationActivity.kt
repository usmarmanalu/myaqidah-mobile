package com.dicoding.myaqidahmobile.ui.home.menu

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.myaqidahmobile.R
import com.dicoding.myaqidahmobile.core.firebasemodel.JanjiTemuDokter
import com.dicoding.myaqidahmobile.core.ui.NotificationAdapter
import com.dicoding.myaqidahmobile.core.utils.DrawableUtils.getWhiteBackArrowDrawable
import com.dicoding.myaqidahmobile.databinding.ActivityNotificationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Suppress("DEPRECATION")
class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple2)

        // Setup action bar
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.notifikasi)
            setHomeAsUpIndicator(getWhiteBackArrowDrawable(this@NotificationActivity))
            setBackgroundDrawable(
                ColorDrawable(ContextCompat.getColor(this@NotificationActivity, R.color.purple2))
            )
        }

        // Fetch notifications
        fetchNotifications()
    }

    private fun fetchNotifications() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            showEmptyState("User tidak ditemukan. Silakan login ulang.")
            return
        }

        db.collection("users").document(userId)
            .collection("dataUser").document(userId)
            .get()
            .addOnSuccessListener { document ->
                val userName = document.getString("name")
                if (userName.isNullOrEmpty()) {
                    showEmptyState("Nama pengguna tidak ditemukan di koleksi 'users'.")
                    return@addOnSuccessListener
                }

                db.collection("registrasi_online")
                    .document(userName)
                    .collection("data_janji_temu")
                    .addSnapshotListener { snapshots, exception ->
                        if (exception != null) {
                            showEmptyState("Terjadi kesalahan: ${exception.message}")
                            return@addSnapshotListener
                        }

                        val notifications = snapshots?.documents?.mapNotNull {
                            it.toObject(JanjiTemuDokter::class.java)
                        }.orEmpty()

                        if (notifications.isEmpty()) {
                            showEmptyState(getString(R.string.no_notifications))
                        } else {
                            setupUI(notifications)
                        }
                    }
            }
            .addOnFailureListener { exception ->
                showEmptyState("Terjadi kesalahan: ${exception.message}")
            }
    }

    private fun setupUI(notifications: List<JanjiTemuDokter>) {
        binding.tvEmptyNotification.visibility = android.view.View.GONE
        binding.rvNotifications.visibility = android.view.View.VISIBLE

        binding.rvNotifications.apply {
            layoutManager = LinearLayoutManager(this@NotificationActivity)
            adapter = NotificationAdapter(notifications)
        }
    }

    private fun showEmptyState(message: String) {
        binding.tvEmptyNotification.apply {
            text = message
            visibility = android.view.View.VISIBLE
        }
        binding.rvNotifications.visibility = android.view.View.GONE
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
