package com.dicoding.myaqidahmobile.ui.home.sectionMenu

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.myaqidahmobile.core.firebasemodel.StepHistory
import com.dicoding.myaqidahmobile.core.ui.StepHistoryAdapter
import com.dicoding.myaqidahmobile.databinding.ActivityStepHistoryBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class StepHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStepHistoryBinding
    private lateinit var stepHistoryAdapter: StepHistoryAdapter
    private val stepHistoryList = mutableListOf<StepHistory>()
    private var stepTarget: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStepHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchStepTargetData()
    }

    private fun setupRecyclerView() {
        stepHistoryAdapter = StepHistoryAdapter(stepHistoryList)
        binding.rvStepHistory.apply {
            layoutManager = LinearLayoutManager(this@StepHistoryActivity)
            adapter = stepHistoryAdapter
        }
    }

    private fun fetchStepTargetData() {
        // Fetch step target value first
        FirebaseFirestore.getInstance().collection("users")
            .document(getUserId()) // Replace with your logic to get the current user's ID
            .get()
            .addOnSuccessListener { document ->
                stepTarget = document.getLong("targetSteps")?.toInt() ?: 0
                fetchFitnessData()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching target steps: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchFitnessData() {
        val userId = getUserId() // Replace with your logic to get the current user's ID
        FirebaseFirestore.getInstance().collection("users").document(userId)
            .collection("fitnessDataGooglefit")
            .get()
            .addOnSuccessListener { result ->
                stepHistoryList.clear() // Clear the existing list
                for (document in result) {
                    val steps = document.getDouble("steps")?.toInt() ?: 0
                    val calories = document.getDouble("calories")?.toInt() ?: 0
                    val date = document.getDate("date") ?: Date()

                    // Format the date as a string
                    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    val formattedDate = dateFormat.format(date)

                    stepHistoryList.add(StepHistory(formattedDate, steps, calories))
                }
                stepHistoryAdapter.notifyDataSetChanged() // Notify adapter for data change
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to fetch history: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getUserId(): String {
        // Replace with your logic to get the current user's ID, e.g., from FirebaseAuth
        return "userId" // Placeholder, change to actual user ID retrieval logic
    }
}
