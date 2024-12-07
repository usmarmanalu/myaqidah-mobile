package com.dicoding.myaqidahmobile.ui.registrasi

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dicoding.myaqidahmobile.*
import com.dicoding.myaqidahmobile.core.firebasemodel.DataKeluargaPasien
import com.dicoding.myaqidahmobile.core.helper.FirebaseRegistrasiOnlineHelper
import com.dicoding.myaqidahmobile.core.utils.DrawableUtils.getWhiteBackArrowDrawable
import com.dicoding.myaqidahmobile.databinding.ActivityDataKeluargaBinding

class DataKeluargaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDataKeluargaBinding
    private lateinit var firebaseHelper: FirebaseRegistrasiOnlineHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataKeluargaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue_soft)

        // Inisialisasi helper Firebase
        firebaseHelper = FirebaseRegistrasiOnlineHelper()

        setupActionBar()
        setupListeners()

        // Memuat data yang sudah disimpan
        loadDataFromFirebase()
    }

    private fun setupActionBar() {
        checkDataStatus { isDataComplete ->
            val title = if (isDataComplete) {
                "Edit Data Keluarga"
            } else {
                "Data Keluarga"
            }

            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                this.title = title
                setHomeAsUpIndicator(getWhiteBackArrowDrawable(this@DataKeluargaActivity))
                setBackgroundDrawable(
                    ColorDrawable(ContextCompat.getColor(this@DataKeluargaActivity, R.color.blue_soft))
                )
            }
        }
    }

    // Fungsi untuk memeriksa status data registrasi dan data keluarga
    private fun checkDataStatus(callback: (Boolean) -> Unit) {
        firebaseHelper.fetchDataKeluarga(
            onSuccess = { dataKeluarga ->
                val isDataComplete = dataKeluarga.nama_keluarga.isNotEmpty() &&
                        dataKeluarga.no_hp.isNotEmpty()
                callback(isDataComplete)
            },
            onFailure = {
                callback(false) // Jika gagal memuat data, dianggap belum lengkap
            }
        )
    }

    private fun setupListeners() {
        binding.btnSimpan.setOnClickListener {
            showSaveConfirmationDialog()
        }
    }

    private fun loadDataFromFirebase() {
        firebaseHelper.fetchDataKeluarga(
            onSuccess = { data ->
                populateFields(data) // Isi field dengan data yang diterima
            },
            onFailure = { exception ->
                Toast.makeText(this, "Gagal memuat data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun populateFields(dataKeluarga: DataKeluargaPasien) {
        binding.edNama.setText(dataKeluarga.nama_keluarga)
        binding.spinnerKeluarga.setSelection(getSpinnerIndex(dataKeluarga.hub_keluarga))
        binding.edAlamatLengkap.setText(dataKeluarga.alamat_lengkap)
        binding.edTelp.setText(dataKeluarga.no_hp)
    }

    private fun getSpinnerIndex(value: String): Int {
        val spinnerArray = resources.getStringArray(R.array.kelurga_array)
        return spinnerArray.indexOf(value).coerceAtLeast(0)
    }

    private fun showSaveConfirmationDialog() {
        AlertDialog.Builder(this)
            .setMessage("Apakah Anda ingin menyimpan data?")
            .setCancelable(false)
            .setPositiveButton("Ya") { _, _ ->
                saveData()
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun saveData() {
        val namaKeluarga = binding.edNama.text.toString().trim()
        val hubunganKeluarga = binding.spinnerKeluarga.selectedItem.toString().trim()
        val alamatLengkap = binding.edAlamatLengkap.text.toString().trim()
        val noHp = binding.edTelp.text.toString().trim()

        if (namaKeluarga.isEmpty() || hubunganKeluarga.isEmpty() || alamatLengkap.isEmpty() || noHp.isEmpty()) {
            Toast.makeText(this, "Mohon lengkapi semua data", Toast.LENGTH_SHORT).show()
            return
        }

        val dataKeluarga = DataKeluargaPasien(
            nama_keluarga = namaKeluarga,
            hub_keluarga = hubunganKeluarga,
            alamat_lengkap = alamatLengkap,
            no_hp = noHp
        )

        firebaseHelper.saveDataKeluarga(
            dataKeluarga,
            onSuccess = {
                Toast.makeText(this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
                navigateToHome()
            },
            onFailure = { exception ->
                Toast.makeText(this, "Gagal menyimpan data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
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
