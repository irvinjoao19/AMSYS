package com.amsys.alphamanfacturas.helper

import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieEntry

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat


class PercentFormat(private var total: Int, private var pieChart: PieChart) : ValueFormatter() {

    var mFormat: DecimalFormat = DecimalFormat("###,###,##0")


    override fun getFormattedValue(value: Float): String {
        return mFormat.format(value)
//        return mFormat.format(total * value / 100).toString() + " %"
    }

    override fun getPieLabel(value: Float, pieEntry: PieEntry): String {
        return if (pieChart.isUsePercentValuesEnabled) {
            getFormattedValue(value)
        } else {
            // raw value, skip percent sign
            mFormat.format(value)
        }
    }

}