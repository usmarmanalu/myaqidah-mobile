package com.dicoding.myaqidahmobile.core.ui

import android.content.*
import android.net.*
import android.view.*
import androidx.recyclerview.widget.*
import com.bumptech.glide.*
import com.dicoding.myaqidahmobile.databinding.*

class BeritaAdapter(private val items: List<Pair<String, String>>) :
    RecyclerView.Adapter<BeritaAdapter.CarouselViewHolder>() {

    inner class CarouselViewHolder(private val binding: ItemBeritaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Pair<String, String>) {
            val (imageUrl, url) = item
            Glide.with(binding.root)
                .load(imageUrl)
                .into(binding.carouselImage)

            binding.root.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding = ItemBeritaBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
