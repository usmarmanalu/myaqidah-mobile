package com.dicoding.myaqidahmobile.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.myaqidahmobile.R
import com.dicoding.myaqidahmobile.core.firebasemodel.StepHistory
import com.dicoding.myaqidahmobile.databinding.ItemStepHistoryBinding
import java.text.SimpleDateFormat
import java.util.*

class StepHistoryAdapter(private val stepHistoryList: MutableList<StepHistory>) :
    RecyclerView.Adapter<StepHistoryAdapter.StepHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepHistoryViewHolder {
        val binding = ItemStepHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return StepHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StepHistoryViewHolder, position: Int) {
        val stepHistory = stepHistoryList[position]
        holder.bind(stepHistory)
    }

    override fun getItemCount() = stepHistoryList.size

    inner class StepHistoryViewHolder(private val binding: ItemStepHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(stepHistory: StepHistory) {
            // Parse the date string to display formatted date
            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val date = dateFormat.parse(stepHistory.date)
            binding.tvDate.text = "Date: ${dateFormat.format(date)}"

            // Set step and calorie values
            binding.tvSteps.text = "Steps: ${stepHistory.steps}"
            binding.tvCalories.text = "Calories: ${stepHistory.calories}"
            binding.tvStepTarget.text = "Target: ${StepHistory.stepTarget}" // Access via companion object

            // Update the status based on whether the target is met
            if (stepHistory.isTargetMet()) {
                binding.tvStatus.text = "Status: terpenuhi"
                binding.tvStatus.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.green)
                )
            } else {
                binding.tvStatus.text = "Status: tidak terpenuhi"
                binding.tvStatus.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.red)
                )
            }
        }
    }

    // Method to update or add a StepHistory entry by date
    fun updateOrAddStepHistory(stepHistory: StepHistory) {
        val existingIndex = stepHistoryList.indexOfFirst { it.date == stepHistory.date }
        if (existingIndex != -1) {
            // Update the existing entry
            stepHistoryList[existingIndex] = stepHistory
            notifyItemChanged(existingIndex)
        } else {
            // Add new entry
            stepHistoryList.add(stepHistory)
            notifyItemInserted(stepHistoryList.size - 1)
        }
    }
}
