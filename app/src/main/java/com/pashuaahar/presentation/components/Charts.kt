package com.pashuaahar.presentation.components

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlin.math.roundToInt

@Composable
fun SavingsBarChart(homemade: Double, market: Double) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        factory = { context -> BarChart(context) },
        update = { chart ->
            val entries = listOf(
                BarEntry(0f, homemade.toFloat()),
                BarEntry(1f, market.toFloat())
            )
            val dataSet = BarDataSet(entries, "Daily Cost").apply {
                colors = listOf(Color.parseColor("#2E7D32"), Color.parseColor("#FF8F00"))
            }
            chart.data = BarData(dataSet)
            chart.xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return when (value.roundToInt()) {
                        0 -> "Homemade"
                        else -> "Market"
                    }
                }
            }
            chart.description.isEnabled = false
            chart.invalidate()
        }
    )
}

@Composable
fun SavingsPieChart(homemade: Double, savings: Double) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        factory = { context -> PieChart(context) },
        update = { chart ->
            val entries = listOf(
                PieEntry(homemade.toFloat(), "Feed Cost"),
                PieEntry(savings.toFloat().coerceAtLeast(0f), "Savings")
            )
            val dataSet = PieDataSet(entries, "Monthly").apply {
                colors = listOf(Color.parseColor("#2E7D32"), Color.parseColor("#FF8F00"))
            }
            chart.data = PieData(dataSet)
            chart.description.isEnabled = false
            chart.invalidate()
        }
    )
}

@Composable
fun SavingsLineChart(monthlySavings: Double) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        factory = { context -> LineChart(context) },
        update = { chart ->
            val entries = (1..12).map { month ->
                Entry(month.toFloat(), (monthlySavings * month).toFloat())
            }
            val dataSet = LineDataSet(entries, "Yearly Savings").apply {
                color = Color.parseColor("#2E7D32")
                setCircleColor(Color.parseColor("#FF8F00"))
            }
            chart.data = LineData(dataSet)
            chart.description.isEnabled = false
            chart.invalidate()
        }
    )
}
