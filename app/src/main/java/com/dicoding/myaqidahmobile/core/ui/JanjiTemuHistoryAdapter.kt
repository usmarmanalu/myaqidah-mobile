package com.dicoding.myaqidahmobile.core.ui

import android.view.*
import androidx.recyclerview.widget.*
import com.bumptech.glide.*
import com.dicoding.myaqidahmobile.core.firebasemodel.*
import com.dicoding.myaqidahmobile.databinding.*
import com.dicoding.myaqidahmobile.ui.home.sectionMenu.*

class JanjiTemuHistoryAdapter(
    private val activity: JanjiTemuHistoryActivity,
    private val janjiTemuList: List<JanjiTemuDokter>
) : RecyclerView.Adapter<JanjiTemuHistoryAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemJanjiTemuHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: JanjiTemuDokter) {
            binding.tvInstalasi.text = "Instalasi: " + item.instalasi
            binding.tvPoliklinik.text = "Poliklinik: " + item.poliklinik
            binding.tvTitle.text = "Janji Temu Dokter: "+ item.dpjp

            binding.tvTanggalKunjungan.text =
                "Janji temu pada tanggal: " + item.tanggalKunjungan + " "
            binding.tvJamKunjungan.text = "pkl: " + item.jamKunjungan

            itemView.setOnClickListener {
                // Show bottom sheet with full details
                val bottomSheet = JanjiTemuDetailBottomSheet(item)
                bottomSheet.show(activity.supportFragmentManager, bottomSheet.tag)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemJanjiTemuHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(janjiTemuList[position])
    }

    override fun getItemCount(): Int = janjiTemuList.size
}
