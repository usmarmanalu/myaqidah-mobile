package com.dicoding.myaqidahmobile.core.ui

import android.view.*
import androidx.recyclerview.widget.*
import com.dicoding.myaqidahmobile.core.firebasemodel.*
import com.dicoding.myaqidahmobile.databinding.*

class BedKttAdapter(private val listBedKtt: ArrayList<BedKTT>) :
    RecyclerView.Adapter<BedKttAdapter.ListViewHolder>() {

    inner class ListViewHolder(private val binding: ItemBedKttBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bedKTT: BedKTT) {
            binding.tvRoomType.text = bedKTT.jenis_kamar
            binding.tvKapasitas.text = bedKTT.kapasitas.toString()
            binding.tvDitempati.text = bedKTT.ditempati.toString()
            binding.tvTersedia.text = bedKTT.tersedia.toString()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = ItemBedKttBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listBedKtt[position])
    }

    override fun getItemCount(): Int = listBedKtt.size
}
