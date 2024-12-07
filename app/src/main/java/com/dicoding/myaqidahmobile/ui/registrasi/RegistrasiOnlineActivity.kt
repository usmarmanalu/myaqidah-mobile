package com.dicoding.myaqidahmobile.ui.registrasi

import android.annotation.*
import android.app.*
import android.content.*
import android.graphics.drawable.*
import android.os.*
import android.text.*
import android.view.*
import android.widget.*
import androidx.activity.*
import androidx.appcompat.app.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.*
import androidx.lifecycle.*
import com.dicoding.myaqidahmobile.R
import com.dicoding.myaqidahmobile.core.firebasemodel.*
import com.dicoding.myaqidahmobile.core.helper.*
import com.dicoding.myaqidahmobile.core.utils.DrawableUtils.getWhiteBackArrowDrawable
import com.dicoding.myaqidahmobile.databinding.*
import kotlinx.coroutines.*
import java.util.*

@Suppress("DEPRECATION")
class RegistrasiOnlineActivity : AppCompatActivity() {

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val viewModel: PlaceViewModel by viewModels()

    private lateinit var binding: ActivityRegistrasiOnlineBinding
    private lateinit var firebaseHelper: FirebaseRegistrasiOnlineHelper

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrasiOnlineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue_soft)

        // Setup Firebase helper
        firebaseHelper = FirebaseRegistrasiOnlineHelper()

        setupActionBar()
        // Memuat data registrasi jika tersedia
        loadDataRegistrasi()

        // Generate nomor rekam medis jika belum ada
        if (binding.edRekamMedis.text.isNullOrEmpty()) {
            binding.edRekamMedis.setText(generateNoRekamMedis())
            binding.edRekamMedis.isEnabled = false
        }

        // Tombol simpan
        binding.btnSimpan.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Apakah Anda ingin menyimpan data dan melanjutkan ke langkah berikutnya?")
                .setCancelable(false)
                .setPositiveButton("Ya") { _, _ -> saveDataRegistrasi() }
                .setNegativeButton("Tidak") { dialog, _ -> dialog.dismiss() }
            builder.create().show()
        }

        binding.editDateOfBirth.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                binding.editDateOfBirth.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
            }, year, month, day).show()
        }

        binding.edPlace.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                lifecycleScope.launch {
                    viewModel.queryChannel.value = s.toString()
                }
            }
        })

        viewModel.searchResult.observe(this) { placesItem ->
            if (placesItem.isNullOrEmpty()) {
                Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show()
            } else {
                val placesName = placesItem.map { it.placeName }
                val adapter =
                    ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, placesName)
                binding.edPlace.setAdapter(adapter)
            }
        }
    }

    private fun setupActionBar() {
        checkDataStatus { isDataComplete ->
            val title = if (isDataComplete) {
                "Edit Registrasi Online"
            } else {
                "Registrasi Online"
            }

            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                this.title = title
                setHomeAsUpIndicator(getWhiteBackArrowDrawable(this@RegistrasiOnlineActivity))
                setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            this@RegistrasiOnlineActivity,
                            R.color.blue_soft
                        )
                    )
                )
            }
        }
    }

    // Fungsi untuk memeriksa status data registrasi dan data keluarga
    private fun checkDataStatus(callback: (Boolean) -> Unit) {
        firebaseHelper.fetchDataRegistrasi(
            onSuccess = { dataRegistrasiOnline ->
                val isDataComplete = dataRegistrasiOnline.nik.isNotEmpty() &&
                        dataRegistrasiOnline.nama_pasien.isNotEmpty()
                callback(isDataComplete)
            },
            onFailure = {
                callback(false) // Jika gagal memuat data, dianggap belum lengkap
            }
        )
    }

    private fun saveDataRegistrasi() {
        // Validasi input
        val noRekamMedis = binding.edRekamMedis.text.toString().trim()
        val nik = binding.edNik.text.toString().trim()
        val namaPasien = binding.editNameRegistrasi.text.toString().trim()
        val golonganDarah = binding.spinnerGoldar.selectedItem.toString().trim()
        val tempatLahir = binding.edTempatLahir.text.toString().trim()
        val tanggalLahir = binding.editDateOfBirth.text.toString().trim()
        val noHp = binding.noHpValue.text.toString().trim()
        val jenisKelamin = binding.spinnerGender.selectedItem.toString().trim()
        val agama = binding.spinnerAgama.selectedItem.toString().trim()
        val kabKota = binding.edPlace.text.toString().trim()
        val alamatLengkap = binding.edAlamatLengkap.text.toString().trim()
        val nasabah = binding.spinnerNasabah.selectedItem.toString().trim()
        val status = binding.spinnerNikah.selectedItem.toString().trim()
        val noKartuJkn = binding.edJkn.text.toString().trim()

        // Pastikan input tidak kosong
        if (noRekamMedis.isEmpty() || nik.isEmpty() || namaPasien.isEmpty()) {
            Toast.makeText(this, "Mohon lengkapi semua data", Toast.LENGTH_SHORT).show()
            return
        }

        // Buat objek data
        val registrasiOnline = RegistrasiOnline(
            no_rekam_medis = noRekamMedis,
            nik = nik,
            nama_pasien = namaPasien,
            golongan_darah = golonganDarah,
            tempat_lahir = tempatLahir,
            tanggal_lahir = tanggalLahir,
            no_hp = noHp,
            jenis_kelamin = jenisKelamin,
            agama = agama,
            kab_kota = kabKota,
            alamat_lengkap = alamatLengkap,
            nasabah = nasabah,
            status = status,
            no_kartu_jkn = noKartuJkn
        )

        // Simpan data ke Firebase Firestore
        firebaseHelper.saveDataRegistrasiOnline(
            userData = registrasiOnline,
            onSuccess = {
                Toast.makeText(this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
                navigateToNextStep()
            },
            onFailure = { exception ->
                Toast.makeText(
                    this,
                    "Gagal menyimpan data: ${exception.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        )
    }

    private fun loadDataRegistrasi() {
        firebaseHelper.fetchDataRegistrasi(
            onSuccess = { data ->
                // Data permanen yang tidak bisa diubah
                binding.edRekamMedis.setText(data.no_rekam_medis.toString())
                binding.edRekamMedis.isEnabled =
                    false // Menonaktifkan field untuk tidak bisa diubah

                binding.edNik.setText(data.nik)
                binding.edNik.isEnabled = false // Menonaktifkan field untuk tidak bisa diubah

                binding.editNameRegistrasi.setText(data.nama_pasien)
                binding.editNameRegistrasi.isEnabled =
                    false // Menonaktifkan field untuk tidak bisa diubah

                binding.spinnerGoldar.setSelection(
                    getSpinnerIndex(
                        binding.spinnerGoldar,
                        data.golongan_darah
                    )
                )
                binding.spinnerGoldar.isEnabled =
                    false // Menonaktifkan spinner untuk tidak bisa diubah

                binding.edTempatLahir.setText(data.tempat_lahir)
                binding.edTempatLahir.isEnabled =
                    false // Menonaktifkan field untuk tidak bisa diubah

                binding.editDateOfBirth.setText(data.tanggal_lahir)
                binding.editDateOfBirth.isEnabled =
                    false // Menonaktifkan field untuk tidak bisa diubah

                binding.spinnerGender.setSelection(
                    getSpinnerIndex(
                        binding.spinnerGender,
                        data.jenis_kelamin
                    )
                )
                binding.spinnerGender.isEnabled =
                    false // Menonaktifkan spinner untuk tidak bisa diubah

                binding.edJkn.setText(data.no_kartu_jkn)
                binding.edJkn.isEnabled = false // Menonaktifkan field untuk tidak bisa diubah

                // Data yang bisa diubah
                binding.noHpValue.setText(data.no_hp) // Editable
                binding.spinnerAgama.setSelection(
                    getSpinnerIndex(
                        binding.spinnerAgama,
                        data.agama
                    )
                ) // Editable
                binding.edPlace.setText(data.kab_kota) // Editable
                binding.edAlamatLengkap.setText(data.alamat_lengkap) // Editable
                binding.spinnerNasabah.setSelection(
                    getSpinnerIndex(
                        binding.spinnerNasabah,
                        data.nasabah
                    )
                ) // Editable
                binding.spinnerNikah.setSelection(
                    getSpinnerIndex(
                        binding.spinnerNikah,
                        data.status
                    )
                ) // Editable
            },
            onFailure = { exception ->
                Toast.makeText(this, "Gagal memuat data: ${exception.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        )
    }

    private fun generateNoRekamMedis(): String {
        val timestamp = System.currentTimeMillis()
        return "RM-${timestamp.toString().takeLast(6)}"
    }

    private fun getSpinnerIndex(spinner: Spinner, value: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString() == value) {
                return i
            }
        }
        return 0
    }

    private fun navigateToNextStep() {
        val intent = Intent(this, DataKeluargaActivity::class.java)
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
