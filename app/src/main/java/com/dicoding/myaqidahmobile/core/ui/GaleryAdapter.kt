package com.dicoding.myaqidahmobile.core.ui

import android.view.*
import androidx.recyclerview.widget.*
import com.bumptech.glide.*
import com.dicoding.myaqidahmobile.databinding.ItemGaleryBinding

class GaleryAdapter(private val items: List<Pair<String, String>>) : RecyclerView.Adapter<GaleryAdapter.CarouselViewHolder>() {

    inner class CarouselViewHolder(private val binding: ItemGaleryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Pair<String, String>) {
            val (imageUrl, title) = item

            binding.carouselTitle.text = title

            Glide.with(binding.carouselImage.context)
                .load(imageUrl)
                .into(binding.carouselImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding = ItemGaleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
