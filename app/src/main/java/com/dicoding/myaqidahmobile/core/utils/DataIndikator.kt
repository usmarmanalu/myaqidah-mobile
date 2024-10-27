package com.dicoding.myaqidahmobile.core.utils

data class DataIndikator(
    val name: String,
    val value: Float
)

val indicatorMutu = listOf(
    DataIndikator("Kepatuhan Kebersihan Tangan", 97.0f),
    DataIndikator("Kepatuhan Penggunaan APD", 100.0f),
    DataIndikator("Kepatuhan Identifikasi Pasien", 100.0f),
    DataIndikator("Waktu Tanggap SC Emergensi", 90.0f),
    DataIndikator("Waktu tunggu rawat jalan", 97.0f),
    DataIndikator("Penundaan operasi elektif", 3.0f),
    DataIndikator("Kepatuhan waktu visite dokter", 96.0f),
    DataIndikator("Pelaporan hasil kritis laboratorium", 100.0f),
    DataIndikator("Kepatuhan Penggunaan Formularium Nasional/Formularium RS", 97.0f),
    DataIndikator("Kepatuhan Terhadap Clinical Pathway", 95.0f),
    DataIndikator("Kepatuhan upaya pencegahan risiko pasien jatuh", 100.0f),
    DataIndikator("Kecepatan waktu tanggap terhadap Komplain", 100.0f),
    DataIndikator("Kepuasan pasien", 97.0f),

    )

val indicatorKeselamatan = listOf(
    DataIndikator("Kepatuhan Identifikasi Pasien", 100.0f),
    DataIndikator("Peningkatan Komunikasi Efektif", 100.0f),
    DataIndikator("Peningkatan Keamanan Obat-Obat Yang Harus Diwaspadai", 100.0f),
    DataIndikator(
        "Kepastian Tepat Lokasi Pembedahan, Tepat Prosedur Pembedahan, Tepat Pasien Pembedahan",
        100.0f
    ),
    DataIndikator("Kepatuhan Kebersihan Tangan", 97.0f),
    DataIndikator("Kepatuhan Upaya Pencegahan Risiko Pasien Jatuh", 100.0f),

    )

val indicatorPelayananan = listOf(
    DataIndikator("Ketepatan Pemberian Obat (5 Benar Pemberian Obat)", 100.0f),
)

val indicatorStrategis = listOf(
    DataIndikator("Angka Kematian Bayi Baru Lahir", 1.0f)
)

val indicatorPerbaikan = listOf(
    DataIndikator("Pelaporan IKP ≤ 2X24 Jam", 100.0f)
)



val indicatorManajemen = listOf(
    DataIndikator("Tidak Terdapat IKP", 0.0f)
)