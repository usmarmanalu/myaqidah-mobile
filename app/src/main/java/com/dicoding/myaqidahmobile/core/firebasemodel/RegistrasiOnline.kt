package com.dicoding.myaqidahmobile.core.firebasemodel

data class RegistrasiOnline(
    val no_rekam_medis: String = "",
    val nik: String = "",
    val nama_pasien: String = "",
    val golongan_darah: String = "",
    val tempat_lahir: String = "",
    val tanggal_lahir: String = "",
    val no_hp: String = "",
    val jenis_kelamin: String = "",
    val agama: String = "",
    val kab_kota: String = "",
    val alamat_lengkap: String = "",
    val nasabah: String = "",
    val status: String = "",
    val no_kartu_jkn: String = ""
)

data class DataKeluargaPasien(
    val nama_keluarga: String = "",
    val hub_keluarga: String = "",
    val alamat_lengkap: String = "",
    val no_hp: String = ""
)
