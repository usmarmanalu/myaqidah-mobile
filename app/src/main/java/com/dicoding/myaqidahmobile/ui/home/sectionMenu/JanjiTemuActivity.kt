package com.dicoding.myaqidahmobile.ui.home.sectionMenu

import android.annotation.*
import android.app.*
import android.app.AlertDialog
import android.content.*
import android.content.pm.*
import android.graphics.drawable.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.activity.*
import androidx.appcompat.app.*
import androidx.core.app.*
import androidx.core.content.*
import com.dicoding.myaqidahmobile.*
import com.dicoding.myaqidahmobile.R
import com.dicoding.myaqidahmobile.core.firebasemodel.*
import com.dicoding.myaqidahmobile.core.helper.*
import com.dicoding.myaqidahmobile.core.utils.DrawableUtils.getWhiteBackArrowDrawable
import com.dicoding.myaqidahmobile.databinding.*
import com.dicoding.myaqidahmobile.ui.home.menu.*
import com.dicoding.myaqidahmobile.ui.jadwal.*
import org.koin.androidx.viewmodel.ext.android.*
import java.text.*
import java.util.*

class JanjiTemuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJanjiTemuBinding

    private lateinit var firebaseHelper: FirebaseRegistrasiOnlineHelper
    private val doctorViewModel: DoctorViewModel by viewModel()


    @Suppress("DEPRECATION")
    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityJanjiTemuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseHelper = FirebaseRegistrasiOnlineHelper()

        statusKunjungan()
        loadDataPasien()

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple2)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Janji Temu Dokter"
            setHomeAsUpIndicator(getWhiteBackArrowDrawable(this@JanjiTemuActivity))
            setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this@JanjiTemuActivity,
                        R.color.purple2
                    )
                )
            )
        }

        doctorViewModel.getDoctors.observe(this) { doctors ->
            // Memastikan bahwa doctors tidak null dan memiliki data yang valid
            doctors?.data?.let { data ->
                val doctorNames = data.map { it.name }

                // Membuat adapter untuk Spinner dengan daftar nama dokter
                val adapter = ArrayAdapter(
                    this, android.R.layout.simple_spinner_item, doctorNames
                )

                // Menetapkan dropdown view untuk Spinner
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                // Menghubungkan adapter ke Spinner
                binding.spinnerDpjp.adapter = adapter
            }
        }

        doctorViewModel.getDoctors.observe(this) { typePoly ->
            typePoly?.data?.let { data ->
                val poli = data.map { it.type }
                val adapter = ArrayAdapter(
                    this, android.R.layout.simple_spinner_item, poli
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerPoliklinik.adapter = adapter
            }
        }

        binding.edittextTanggalKunjungan.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                binding.edittextTanggalKunjungan.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
            }, year, month, day).show()
        }

        binding.edittextJamKunjungan.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)

                binding.edittextJamKunjungan.setText(formattedTime)
            }, hour, minute, true).show()
        }

        binding.btnDaftar.setOnClickListener {
            firebaseHelper.fetchDataRegistrasi(
                onSuccess = { data ->
                    if (data.no_rekam_medis.toString().isEmpty() || data.nik.isEmpty()) {
                        Toast.makeText(
                            this,
                            "Anda belum melakukan registrasi online",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Konfirmasi")
                        builder.setMessage("Apakah Anda yakin ingin menyimpan data janji temu?")
                        builder.setPositiveButton("Ya") { _, _ ->
                            saveDataJanjiTemuDokter()
                        }
                        builder.setNegativeButton("Batal") { dialog, _ ->
                            dialog.dismiss()
                        }
                        builder.show()
                    }
                },
                onFailure = { exception ->
                    Toast.makeText(
                        this,
                        "Anda belum melakukan registrasi online: ${exception.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }


        binding.btnCancel.setOnClickListener {
            // Kembali ke activity sebelumnya atau menutup activity
            onBackPressed()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadDataPasien() {
        firebaseHelper.fetchDataRegistrasi(
            onSuccess = { data ->
                if (data.nik.isEmpty()) {
                    binding.tvTerdaftar.text = getString(R.string.terdaftar) + " " + "Tidak"
                } else {
                    binding.tvTerdaftar.text = getString(R.string.terdaftar) + " " + "Ya"

                }

                binding.tvGoldar.text =
                    getString(R.string.golongan_darah) + " " + data.golongan_darah

                val birthDate = data.tanggal_lahir
                val (years, months, days) = calculateAgeDetailed(birthDate)
                binding.tvUsia.text = if (years >= 0) {
                    getString(R.string.usia) + "$years tahun, $months bulan, $days hari"
                } else {
                    "Tanggal lahir tidak valid"
                }


                binding.tvJaminan.text = getString(R.string.jaminan) + " " + data.nasabah
                binding.tvNoRekamMedis.text =
                    getString(R.string.no_rekam_medis) + " " + data.no_rekam_medis.toString()
                binding.tvNamaPasien.text = getString(R.string.nama_pasien) + " " + data.nama_pasien
            },
            onFailure = { exception ->
                Toast.makeText(
                    this,
                    "Gagal memuat data pasien: ${exception.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        )
    }

    private fun saveDataJanjiTemuDokter() {
        val instalasi = binding.spinnerInstalasi.selectedItem.toString()
        val poliklinik = binding.spinnerPoliklinik.selectedItem.toString()
        val dpjp = binding.spinnerDpjp.selectedItem.toString()
        val asalRujukan = binding.spinnerAsalRujukan.selectedItem.toString()
        val rujukan_faskes = binding.spinnerRujukanFaskes.selectedItem.toString()
        val cara_bayar = binding.spinnerCaraBayar.selectedItem.toString()
        val perusahaan = binding.spinnerPerusahaan.selectedItem.toString()
        val tanggalKunjungan = binding.edittextTanggalKunjungan.text.toString()
        val jamKunjungan = binding.edittextJamKunjungan.text.toString()

        // Pastikan input tidak kosong
        if (instalasi.isEmpty() || poliklinik.isEmpty() || dpjp.isEmpty() || tanggalKunjungan.isEmpty() || jamKunjungan.isEmpty()) {
            Toast.makeText(this, "Mohon lengkapi semua data", Toast.LENGTH_SHORT).show()
            return
        }

        val currentTimestamp = System.currentTimeMillis()
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = Date(currentTimestamp)
        val formattedDate = sdf.format(date)

        val janjiTemuDokter = JanjiTemuDokter(
            dateTimestamp = formattedDate,
            instalasi = instalasi,
            poliklinik = poliklinik,
            dpjp = dpjp,
            asalRujukan = asalRujukan,
            rujukanFaskes = rujukan_faskes,
            caraBayar = cara_bayar,
            perusahaan = perusahaan,
            tanggalKunjungan = tanggalKunjungan,
            jamKunjungan = jamKunjungan
        )

        firebaseHelper.saveDataJanjiTemu(
            janjiTemu = janjiTemuDokter,
            onSuccess = {
                // Tampilkan Notifikasi
                showNotification(janjiTemuDokter.dpjp.toString())
                // Setelah data disimpan, arahkan ke MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
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

    private fun calculateAgeDetailed(birthDate: String): Triple<Int, Int, Int> {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val birthDateCalendar = Calendar.getInstance()
        val today = Calendar.getInstance()

        try {
            val date = sdf.parse(birthDate)
            birthDateCalendar.time = date!!
        } catch (e: Exception) {
            e.printStackTrace()
            return Triple(-1, -1, -1)
        }

        val years = today.get(Calendar.YEAR) - birthDateCalendar.get(Calendar.YEAR)

        // Koreksi jika belum melewati ulang tahun tahun ini
        if (today.get(Calendar.DAY_OF_YEAR) < birthDateCalendar.get(Calendar.DAY_OF_YEAR)) {
            birthDateCalendar.add(Calendar.YEAR, years)
            if (birthDateCalendar.after(today)) {
                birthDateCalendar.add(Calendar.YEAR, -1)
            }
        }

        val diffInMillis = today.timeInMillis - birthDateCalendar.timeInMillis
        val diffDays = diffInMillis / (1000 * 60 * 60 * 24)

        val months = (diffDays / 30).toInt() % 12
        val days = (diffDays % 30).toInt()

        return Triple(years, months, days)
    }


    private fun statusKunjungan() {
        firebaseHelper.fetchDataJanjiTemu(
            onSuccess = { janjiTemu ->
                // Check if both tanggal and jam are not null and not empty
                if (!janjiTemu.tanggalKunjungan.isNullOrEmpty() && !janjiTemu.jamKunjungan.isNullOrEmpty()) {
                    val tgl = janjiTemu.tanggalKunjungan
                    val jam = janjiTemu.jamKunjungan
                    binding.tvKunjunganTerakhir.text = "Kunjungan Terakhir: tanggal $tgl, pkl $jam"
                } else {
                    binding.tvKunjunganTerakhir.text =
                        getString(R.string.kunjungan_terakhir_belum_ada_kunjungan)
                }
            },
            onFailure = { _ ->
                // Handle the failure and show a generic error message
                binding.tvKunjunganTerakhir.text = "Kunjungan Terakhir: gagal memuat data"
            }
        )
    }


    private fun showNotification(dpjp: String) {
        val channelId = "appointment_channel_id"
        val channelName = "Appointment Notifications"
        val notificationId = 1

        // Periksa izin jika diperlukan
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Jika izin tidak diberikan, minta izin (khusus Android 13 ke atas)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    101
                )
            }
            return
        }

        // membuat channel notifikasi jika izin sudah diberikan
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = "Notifications for doctor appointments"
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, NotificationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo_aqidah_new)
            .setContentTitle("Janji Temu Berhasil Dibuat")
            .setContentText("Janji temu dengan dokter $dpjp telah berhasil dibuat.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(this).notify(notificationId, notification)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_appoinment_history, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuStepHistory -> {
                startActivity(Intent(this, JanjiTemuHistoryActivity::class.java))
                true
            }

            android.R.id.home -> {
                @Suppress("DEPRECATION")
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}