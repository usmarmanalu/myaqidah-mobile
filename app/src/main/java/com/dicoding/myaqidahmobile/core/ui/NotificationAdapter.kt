package com.dicoding.myaqidahmobile.core.ui

import android.content.*
import android.view.*
import androidx.recyclerview.widget.*
import com.dicoding.myaqidahmobile.*
import com.dicoding.myaqidahmobile.core.firebasemodel.*
import com.dicoding.myaqidahmobile.databinding.*
import com.dicoding.myaqidahmobile.ui.home.menu.*
import com.dicoding.myaqidahmobile.ui.home.sectionMenu.*
import java.text.*
import java.util.*

class NotificationAdapter(
    private val notifications: List<JanjiTemuDokter>
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemNotificationBinding.bind(view)
        fun bind(janjiTemuDokter: JanjiTemuDokter) {

            binding.tvTimeNotification.text = janjiTemuDokter.dateTimestamp.toString()
            binding.tvDpjp.text = "Janji Temu oleh Dokter" + " " + janjiTemuDokter.dpjp
            binding.tvInstalasi.text = janjiTemuDokter.instalasi
            binding.tvPoliklinik.text = janjiTemuDokter.poliklinik


            itemView.setOnClickListener {
                val intent = Intent(itemView.context, JanjiTemuHistoryActivity::class.java)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notifications[position])
    }

    override fun getItemCount(): Int = notifications.size
}
