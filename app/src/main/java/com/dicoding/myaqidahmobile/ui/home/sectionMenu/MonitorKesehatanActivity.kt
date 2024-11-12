package com.dicoding.myaqidahmobile.ui.home.sectionMenu

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dicoding.myaqidahmobile.R
import com.dicoding.myaqidahmobile.core.utils.*
import com.dicoding.myaqidahmobile.core.utils.DrawableUtils.getWhiteBackArrowDrawable
import com.dicoding.myaqidahmobile.databinding.ActivityMonitorKesehatanBinding
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.result.DataReadResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import java.util.concurrent.TimeUnit

class MonitorKesehatanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMonitorKesehatanBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var googleAccount: GoogleSignInAccount? = null
    private val stepsData = mutableListOf<Entry>()
    private val caloriesData = mutableListOf<Entry>()
    private var stepTarget = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonitorKesehatanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeFirebase()
        setupActionBar()
        checkAndRequestPermissions()
        loadTargetData()
        binding.btnSetTarget.setOnClickListener { setStepTarget() }
    }

    private fun initializeFirebase() {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Monitor Kesehatan"
            setHomeAsUpIndicator(getWhiteBackArrowDrawable(this@MonitorKesehatanActivity))
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@MonitorKesehatanActivity, R.color.purple2)))
        }
    }

    @SuppressLint("InlinedApi")
    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), REQUEST_CODE_PERMISSION_ACTIVITY_RECOGNITION)
        } else {
            accessGoogleFit()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION_ACTIVITY_RECOGNITION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            accessGoogleFit()
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun accessGoogleFit() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Fitness.SCOPE_ACTIVITY_READ)
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleAccount = GoogleSignIn.getLastSignedInAccount(this)

        if (googleAccount == null) {
            startActivityForResult(googleSignInClient.signInIntent, REQUEST_CODE_SIGN_IN)
        } else {
            fetchFitnessData()
        }
    }

    private fun fetchFitnessData() {
        val endTime = Calendar.getInstance().timeInMillis
        val startTime = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -7) }.timeInMillis

        val readRequest = DataReadRequest.Builder()
            .read(DataType.TYPE_STEP_COUNT_DELTA)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .bucketByTime(1, TimeUnit.DAYS)
            .build()

        googleAccount?.let { account ->
            Fitness.getHistoryClient(this, account)
                .readData(readRequest)
                .addOnSuccessListener { response -> processFitnessData(response) }
                .addOnFailureListener { e -> Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show() }
        }
    }

    private fun processFitnessData(response: DataReadResponse) {
        stepsData.clear()
        caloriesData.clear()
        var dayIndex = 0

        response.buckets.forEach { bucket ->
            var dailySteps = 0
            bucket.dataSets.forEach { dataSet ->
                dataSet.dataPoints.forEach { dataPoint ->
                    dataPoint.dataType.fields.forEach { field ->
                        if (field.name == "steps") dailySteps += dataPoint.getValue(field).asInt()
                    }
                }
            }

            val dailyCalories = (dailySteps * 0.04).toFloat()
            stepsData.add(Entry(dayIndex.toFloat(), dailySteps.toFloat()))
            caloriesData.add(Entry(dayIndex.toFloat(), dailyCalories))
            dayIndex++
        }

        saveFitnessDataToFirebase()
        setupChart()
        updateUiWithLatestData()
    }

    private fun updateUiWithLatestData() {
        val latestStep = stepsData.lastOrNull()?.y ?: 0f
        val latestCalories = caloriesData.lastOrNull()?.y ?: 0f
        binding.tvSteps.text = "Langkah: ${latestStep.toInt()}"
        binding.tvCalories.text = "Kalori terbakar: ${latestCalories.toInt()}"
    }

    private fun setStepTarget() {
        val targetStepsText = binding.etStepTarget.text.toString()
        val targetSteps = targetStepsText.toIntOrNull()
        if (targetSteps == null) {
            binding.etStepTarget.error = "Masukkan target langkah"
            return
        }

        stepTarget = targetSteps
        saveTargetToFirebase(stepTarget)
        binding.tvTargetSteps.text = "Target Langkah: $stepTarget langkah/hari"
    }

    private fun saveTargetToFirebase(targetSteps: Int) {
        val userId = auth.currentUser?.uid ?: return
        val data = mapOf("targetSteps" to targetSteps)
        db.collection("users").document(userId).set(data)
            .addOnSuccessListener { Toast.makeText(this, "Target updated", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { e -> Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_SHORT).show() }
    }

    private fun saveFitnessDataToFirebase() {
        val userId = auth.currentUser?.uid ?: return
        val currentDate = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        val fitnessData = mapOf(
            "steps" to (stepsData.lastOrNull()?.y ?: 0f),
            "calories" to (caloriesData.lastOrNull()?.y ?: 0f),
            "date" to currentDate,
        )

        db.collection("users").document(userId)
            .collection("fitnessDataGooglefit")
            .document(currentDate.toString())
            .set(fitnessData)
            .addOnSuccessListener { Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { e -> Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_SHORT).show() }
    }

    private fun loadTargetData() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                stepTarget = (document["targetSteps"] as? Long)?.toInt() ?: 0
                binding.tvTargetSteps.text = "Target Langkah: $stepTarget langkah/hari"
            }
    }

    private fun setupChart() {
        // Prepare data set for steps
        val lineDataSetSteps = LineDataSet(stepsData, "Steps").apply {
            color = ContextCompat.getColor(this@MonitorKesehatanActivity, R.color.blue)
            valueTextColor = ContextCompat.getColor(this@MonitorKesehatanActivity, R.color.white)
            valueTextSize = 12f
            lineWidth = 2f
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawCircles(false)
            setDrawValues(false)
        }

        // Prepare data set for calories
        val lineDataSetCalories = LineDataSet(caloriesData, "Calories").apply {
            color = ContextCompat.getColor(this@MonitorKesehatanActivity, R.color.red)
            valueTextColor = ContextCompat.getColor(this@MonitorKesehatanActivity, R.color.white)
            valueTextSize = 12f
            lineWidth = 2f
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawCircles(false)
            setDrawValues(false)
        }

        // Combine both datasets into LineData
        val lineData = LineData(lineDataSetSteps, lineDataSetCalories)

        // Setup the chart
        binding.chart.apply {
            data = lineData // Set data

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = MyXAxisValueFormatter()
                granularity = 1f
                isGranularityEnabled = true
                setLabelCount(7, true)
                textColor = ContextCompat.getColor(this@MonitorKesehatanActivity, R.color.black)
                setDrawGridLines(false)
            }

            axisLeft.apply {
                axisMinimum = 0f
                granularity = 1f
                textColor = ContextCompat.getColor(this@MonitorKesehatanActivity, R.color.black)
            }

            axisRight.isEnabled = false

            legend.apply {
                isEnabled = true
                textColor = ContextCompat.getColor(this@MonitorKesehatanActivity, R.color.black)
                form = Legend.LegendForm.LINE
            }

            description.isEnabled = false
            animateX(1000)

            invalidate()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_step_history, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuStepHistory -> {
                startActivity(Intent(this, StepHistoryActivity::class.java))
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val REQUEST_CODE_SIGN_IN = 100
        private const val REQUEST_CODE_PERMISSION_ACTIVITY_RECOGNITION = 101
    }
}
