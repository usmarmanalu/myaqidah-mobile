package com.dicoding.myaqidahmobile.core.ui

import android.os.*
import android.view.*
import com.dicoding.myaqidahmobile.core.firebasemodel.*
import com.dicoding.myaqidahmobile.databinding.*
import com.google.android.material.bottomsheet.*

class JanjiTemuDetailBottomSheet(private val janjiTemu: JanjiTemuDokter) :
    BottomSheetDialogFragment() {

    private var _binding: BottomSheetJanjiTemuDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetJanjiTemuDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set data to views
        binding.apply {
            tvInstalasi.text = janjiTemu.instalasi
            tvPoliklinik.text = janjiTemu.poliklinik
            tvDpjp.text = janjiTemu.dpjp
            tvAsalRujukan.text = janjiTemu.asalRujukan ?: "N/A"
            tvRujukanFaskes.text = janjiTemu.rujukanFaskes ?: "N/A"
            tvCaraBayar.text = janjiTemu.caraBayar ?: "N/A"
            tvPerusahaan.text = janjiTemu.perusahaan ?: "N/A"
            tvTanggalKunjungan.text = janjiTemu.tanggalKunjungan ?: "N/A"
            tvJamKunjungan.text = janjiTemu.jamKunjungan ?: "N/A"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
