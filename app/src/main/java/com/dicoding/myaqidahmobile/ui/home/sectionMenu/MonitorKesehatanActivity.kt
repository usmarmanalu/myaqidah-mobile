package com.dicoding.myaqidahmobile.ui.home.sectionMenu

import android.Manifest
import android.annotation.*
import android.content.*
import android.content.pm.*
import android.graphics.drawable.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.appcompat.app.*
import androidx.core.app.*
import androidx.core.content.*
import com.dicoding.myaqidahmobile.R
import com.dicoding.myaqidahmobile.core.helper.*
import com.dicoding.myaqidahmobile.core.utils.*
import com.dicoding.myaqidahmobile.core.utils.DrawableUtils.getWhiteBackArrowDrawable
import com.dicoding.myaqidahmobile.databinding.*
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.*
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.fitness.*
import com.google.android.gms.fitness.data.*
import com.google.android.gms.fitness.request.*
import com.google.android.gms.fitness.result.*
import java.util.*
import java.util.concurrent.*

@Suppress("DEPRECATION")
class MonitorKesehatanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMonitorKesehatanBinding
    private lateinit var firebaseAuthHelper: FirebaseAuthHelper

    private var googleAccount: GoogleSignInAccount? = null
    private val stepsData = mutableListOf<Entry>()
    private val caloriesData = mutableListOf<Entry>()
    private var stepTarget = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonitorKesehatanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuthHelper = FirebaseAuthHelper()

        setupActionBar()
        checkAndRequestPermissions()

        binding.btnSetTarget.setOnClickListener { setStepTarget() }
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Monitor Kesehatan"
            setHomeAsUpIndicator(getWhiteBackArrowDrawable(this@MonitorKesehatanActivity))
            setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this@MonitorKesehatanActivity,
                        R.color.purple2
                    )
                )
            )
        }
    }

    @SuppressLint("InlinedApi")
    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                REQUEST_CODE_PERMISSION_ACTIVITY_RECOGNITION
            )
        } else {
            accessGoogleFit()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION_ACTIVITY_RECOGNITION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            accessGoogleFit()
        } else {
            Toast.makeText(this, "Akses diizinkan", Toast.LENGTH_SHORT).show()
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
                .addOnFailureListener { _ ->
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
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

            // Ensure that daily steps and calories are not negative before adding them
            if (dailySteps >= 0 && dailyCalories >= 0) {
                stepsData.add(Entry(dayIndex.toFloat(), dailySteps.toFloat()))
                caloriesData.add(Entry(dayIndex.toFloat(), dailyCalories))
                dayIndex++
            }
        }

        updateUiWithLatestData()
        setupChart()
    }


    @SuppressLint("SetTextI18n")
    private fun updateUiWithLatestData() {
        val latestStep = stepsData.lastOrNull()?.y ?: 0f
        val latestCalories = caloriesData.lastOrNull()?.y ?: 0f
        binding.tvSteps.text = "Total langkah hari ini: ${latestStep.toInt()}"
        binding.tvCalories.text = "Total Kalori hari ini: ${latestCalories.toInt()}"
    }

    private fun saveTargetToFirebase(targetSteps: Int) {
        val latestStep = stepsData.lastOrNull()?.y ?: 0f

        firebaseAuthHelper.saveTargetData(
            stepTarget = targetSteps,
            stepsAchieved = latestStep,
            onSuccess = {
                Toast.makeText(this, "Berhasil tersimpan", Toast.LENGTH_SHORT).show()
            },
            onFailure = {
                Toast.makeText(this, "Gagal ${it.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    @SuppressLint("SetTextI18n")
    private fun setStepTarget() {
        val targetStepsText = binding.etStepTarget.text.toString()

        if (targetStepsText.isBlank()) {
            binding.etStepTarget.error = "Masukkan target langkah"
            return
        }

        val targetSteps = try {
            targetStepsText.toInt()
        } catch (e: NumberFormatException) {
            binding.etStepTarget.error = "Masukkan angka yang valid"
            return
        }

        stepTarget = targetSteps
        saveTargetToFirebase(stepTarget)

        binding.tvTargetSteps.text = "Target Langkah: $stepTarget langkah/hari"
        binding.etStepTarget.text.clear()
    }

    private fun setupChart() {
        // Prepare data set for steps
        val lineDataSetSteps = LineDataSet(stepsData, "Langkah").apply {
            color = ContextCompat.getColor(this@MonitorKesehatanActivity, R.color.blue)
            valueTextColor = ContextCompat.getColor(this@MonitorKesehatanActivity, R.color.white)
            valueTextSize = 12f
            lineWidth = 2f
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawCircles(false)
            setDrawValues(false)
        }

        // Prepare data set for calories
        val lineDataSetCalories = LineDataSet(caloriesData, "Kalori").apply {
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
                setLabelCount(stepsData.size, true)
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

            invalidate() // Refresh the chart
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