package com.example.tradingview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import org.json.JSONObject

class GraphViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph_viewer)

        val lineChart: LineChart = findViewById(R.id.lineChart)
        val historicalDataString = intent.getStringExtra("historicalData")

        if (historicalDataString != null) {
            val historicalData = JSONObject(historicalDataString)
            val timeSeries = historicalData.getJSONObject("Time Series (Daily)")
            val entries: ArrayList<Entry> = ArrayList()
            val dates: ArrayList<String> = ArrayList() // If you need to use the dates

            var index = 0f // Use a float index for the x-axis
            timeSeries.keys().forEach { date ->
                val dailyData = timeSeries.getJSONObject(date)
                val closePrice = dailyData.getString("4. close").toFloat()
                entries.add(Entry(index, closePrice))
                dates.add(date) // Storing the date if needed for labels
                index++
            }

            // Since we're iterating in an arbitrary order, sort the entries by their x value
            entries.sortBy { it.x }

            val dataSet = LineDataSet(entries, "Close Price")
            styleDataSet(dataSet) // A method to style your dataSet

            val lineData = LineData(dataSet)
            lineChart.data = lineData
            lineChart.invalidate() // Refresh the chart
        } else {
            // Handle case where historicalDataString is null
        }
    }

    private fun styleDataSet(dataSet: LineDataSet) {
        dataSet.color = ContextCompat.getColor(this, R.color.design_default_color_primary)
        dataSet.valueTextColor = ContextCompat.getColor(this, R.color.design_default_color_primary_dark)
        // Add more styling options as needed
    }
}