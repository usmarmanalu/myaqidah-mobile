package com.dicoding.myaqidahmobile.ui.home.sectionMenu

import android.graphics.drawable.*
import android.os.*
import android.view.*
import androidx.activity.*
import androidx.appcompat.app.*
import androidx.core.content.*
import androidx.databinding.*
import com.dicoding.myaqidahmobile.*
import com.dicoding.myaqidahmobile.core.utils.*
import com.dicoding.myaqidahmobile.core.utils.DrawableUtils.getWhiteBackArrowDrawable
import com.dicoding.myaqidahmobile.databinding.*
import com.github.mikephil.charting.charts.*
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.*
import com.github.mikephil.charting.highlight.*
import com.github.mikephil.charting.listener.*
import com.github.mikephil.charting.utils.*

class IndicatorMutuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIndicatorMutuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_indicator_mutu)

        setupBarChart(indicatorMutu)

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple2)

        // Setup action bar with title and back button
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Indikator Mutu RS Aqidah"
            setHomeAsUpIndicator(getWhiteBackArrowDrawable(this@IndicatorMutuActivity))
            setBackgroundDrawable(
                ColorDrawable(ContextCompat.getColor(this@IndicatorMutuActivity, R.color.purple2))
            )
        }

        // Setup the PieCharts using a helper function
        setupDonutChart(binding.barChartKeselamatan, indicatorKeselamatan, "")
        setupDonutChart(binding.barChartKlinis, indicatorPelayananan, "")
        setupDonutChart(binding.barChartStrategis, indicatorStrategis, "")
        setupDonutChart(binding.barChartPerbaikan, indicatorPerbaikan, "")
        setupDonutChart(binding.barChartManajement, indicatorManajemen, "")
    }

    private fun setupBarChart(indicators: List<DataIndikator>) {
        val barEntries = indicators.mapIndexed { index, indicator ->
            BarEntry(index.toFloat(), indicator.value)
        }

        val dataSet = BarDataSet(barEntries, "Indikator Mutu")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

        val barData = BarData(dataSet)
        barData.barWidth = 0.5f  // Adjust bar width

        val chart: BarChart = findViewById(R.id.barChart_Mutu)
        chart.data = barData
        chart.invalidate()  // Refresh the chart

        chart.animateY(1000)  // Animation for 1 second

        // Hide X-axis labels initially
        val xAxis = chart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(indicators.map { it.name })
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.setDrawLabels(false)  // Initially hide the labels
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        // Click listener for bar entries
        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e is BarEntry) {
                    // Get the index of the clicked bar
                    val index = e.x.toInt()

                    // Get the name of the indicator corresponding to the clicked bar
                    val indicatorName = indicators[index].name
                    val indicatorValue = indicators[index].value

                    AlertDialog.Builder(this@IndicatorMutuActivity)
                        .setTitle("Indikator Mutu")
                        .setMessage("$indicatorName: $indicatorValue")
                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        .show()

                    // Update the chart to reflect the changes
                    chart.invalidate()
                }
            }

            override fun onNothingSelected() {
                // Hide the labels again when nothing is selected
                xAxis.setDrawLabels(false)
                chart.invalidate()  // Refresh the chart
            }
        })

        // Set up other chart properties
        chart.setScaleEnabled(false)
        chart.description.isEnabled = false
        chart.setFitBars(true)
        chart.xAxis.axisMinimum = -0.5f
        chart.xAxis.axisMaximum = indicators.size - 0.5f
    }

    private fun setupDonutChart(
        pieChart: PieChart,
        indicators: List<DataIndikator>,
        label: String
    ) {
        // Create a list of entries for the PieChart
        val entries = indicators.map {
            PieEntry(it.value, it.name)
        }.toMutableList() // Make it mutable to add an entry if needed

        val dataSet = PieDataSet(entries, label)
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.sliceSpace = 3f // Space between slices
        dataSet.valueTextColor = android.graphics.Color.BLACK // Text color for values
        dataSet.valueTextSize = 16f // Text size for values

        val pieData = PieData(dataSet)
        pieChart.data = pieData
        pieChart.setUsePercentValues(false) // Disable percentage display
        pieChart.description.isEnabled = false // Disable chart description
        pieChart.setDrawHoleEnabled(true) // Enable hole for donut effect
        pieChart.holeRadius = 60f // Percentage of hole radius
        pieChart.setHoleColor(android.graphics.Color.WHITE) // Color of the hole
        pieChart.animateY(1000) // Chart animation

        // Set the center text to show "0" if total value is 0
        val totalValue = indicators.sumOf { it.value.toDouble() }.toFloat()
        pieChart.centerText = if (totalValue == 0f) "0" else totalValue.toString()
        pieChart.setCenterTextSize(20f) // Set the size of the center text
        pieChart.setCenterTextColor(android.graphics.Color.BLACK)

        pieChart.invalidate() // Refresh chart
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
