package com.dicoding.myaqidahmobile.core.ui

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.myaqidahmobile.R
import com.dicoding.myaqidahmobile.core.domain.model.JadwalDoctors
import com.dicoding.myaqidahmobile.databinding.ItemDoctorBinding

class DoctorAdapter(
    private val context: Context,
) : ListAdapter<JadwalDoctors, DoctorAdapter.ListViewHolder>(DIFF_CALLBACK) {

    var onItemClick: ((JadwalDoctors) -> Unit)? = null
    var onFavoriteClick: ((JadwalDoctors) -> Unit)? = null


    inner class ListViewHolder(private val binding: ItemDoctorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: JadwalDoctors) {
            Glide.with(itemView.context)
                .load(data.image)
                .into(binding.imageDoctor)

            binding.textDoctorName.text = data.name
            binding.textSchedule.text = data.schedule
            binding.textType.text = data.type

            val newStatus = data.isFavorite
            setStatusFavorite(newStatus)

            binding.imageFavorite.setOnClickListener {
                data.isFavorite = !newStatus
                onFavoriteClick?.invoke(data)
                setStatusFavorite(data.isFavorite)

                Toast.makeText(
                    itemView.context,
                    if (newStatus) "Berhasil ditambahkan ke favorite" else "Berhasil dihapus dari favorite",
                    Toast.LENGTH_SHORT
                ).show()
            }

            itemView.setOnClickListener {
                onItemClick?.invoke(data)
                showScheduleDialog(data.schedule)
            }
        }

        private fun setStatusFavorite(statusFavorite: Boolean) {
            if (statusFavorite) {
                binding.imageFavorite.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favorite_filled))
            } else {
                binding.imageFavorite.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favorite_outlined))
            }
        }
    }

    private fun showScheduleDialog(schedule: String) {
        AlertDialog.Builder(context)
            .setTitle("Jadwal Dokter Spesialis")
            .setMessage(schedule)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<JadwalDoctors> =
            object : DiffUtil.ItemCallback<JadwalDoctors>() {
                override fun areItemsTheSame(
                    oldItem: JadwalDoctors,
                    newItem: JadwalDoctors
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: JadwalDoctors,
                    newItem: JadwalDoctors
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
