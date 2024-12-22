package com.dicoding.myaqidahmobile.core.ui

import android.app.*
import android.content.*
import android.view.*
import androidx.core.app.*
import androidx.core.util.*
import androidx.recyclerview.widget.*
import com.bumptech.glide.*
import com.dicoding.myaqidahmobile.core.firebasemodel.*
import com.dicoding.myaqidahmobile.databinding.*
import com.dicoding.myaqidahmobile.ui.home.*

class ArtikelAdapter(private val listArtikel: ArrayList<Artikel>) :
    RecyclerView.Adapter<ArtikelAdapter.ListViewHolder>() {

    inner class ListViewHolder(private val binding: ItemArtikelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(listArtikel: Artikel) {

            binding.tvTitle.text = listArtikel.title
            binding.tvArtikel.text = listArtikel.artikel

            Glide.with(binding.ivArtikel.context)
                .load(listArtikel.image)
                .into(binding.ivArtikel)


            itemView.setOnClickListener {
                val intent = Intent(itemView.context, ArtikelActivity::class.java).apply {
                    putExtra("artikel", listArtikel)
                }

                // Use androidx.core.util.Pair here for transitions
                val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    itemView.context as Activity,
                    Pair(binding.ivArtikel, "image"),
                    Pair(binding.tvTitle, "title"),
                    Pair(binding.tvArtikel, "artikel"),
                )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = ItemArtikelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtikelAdapter.ListViewHolder, position: Int) {
        holder.bind(listArtikel[position])
    }

    override fun getItemCount(): Int = listArtikel.size

}