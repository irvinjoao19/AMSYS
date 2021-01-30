package com.amsys.alphamanfacturas.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.amsys.alphamanfacturas.R
import com.amsys.alphamanfacturas.data.local.model.Query
import com.amsys.alphamanfacturas.data.local.model.Reporte
import com.amsys.alphamanfacturas.data.viewModel.ReporteViewModel
import com.amsys.alphamanfacturas.data.viewModel.ViewModelFactory
import com.amsys.alphamanfacturas.helper.PercentFormat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MainFragment : DaggerFragment(), OnChartValueSelectedListener,
    SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var reporteViewModel: ReporteViewModel

    private var token: String = ""
    private var usuarioId: Int = 0
    lateinit var q: Query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        q = Query()
        arguments?.let {
            token = it.getString(ARG_PARAM1)!!
            usuarioId = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        reporteViewModel =
            ViewModelProvider(this, viewModelFactory).get(ReporteViewModel::class.java)

        q.userId = usuarioId

        setChartConfiguration(anyAvisos)
        setChartConfiguration(anyInspeccion)

        reporteViewModel.getReporte(token, q)
        reporteViewModel.avisos.observe(viewLifecycleOwner) {
            if (it != null){
                setData(anyAvisos, it)
            }
        }

        reporteViewModel.inspecciones.observe(viewLifecycleOwner) {
            if (it != null){
                setData(anyInspeccion, it)
            }
        }

        reporteViewModel.loading.observe(viewLifecycleOwner, {
            if (!it) {
                refreshLayout.isRefreshing = false
            }
        })

        refreshLayout.setOnRefreshListener(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: Int) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        if (e == null)
            return
        Log.i(
            "VAL SELECTED",
            "Value: " + e.y + ", index: " + h?.x
                    + ", DataSet index: " + h?.dataSetIndex
        )
    }

    override fun onNothingSelected() {
        Log.i("PieChart", "nothing selected")
    }

    private fun setChartConfiguration(anyPie: PieChart) {
        anyPie.setUsePercentValues(true)
        anyPie.description.isEnabled = false
        anyPie.setExtraOffsets(5f, 10f, 5f, 5f)
        anyPie.dragDecelerationFrictionCoef = 0.95f
        anyPie.isDrawHoleEnabled = false
        anyPie.setHoleColor(Color.WHITE)
        anyPie.setTransparentCircleColor(Color.WHITE)
        anyPie.setTransparentCircleAlpha(110)
        anyPie.holeRadius = 0f
        anyPie.transparentCircleRadius = 0f
        anyPie.setDrawCenterText(true)
        anyPie.rotationAngle = 0f
        anyPie.isRotationEnabled = false
        anyPie.isHighlightPerTapEnabled = true
        anyPie.setOnChartValueSelectedListener(this)
        anyPie.animateY(1400, Easing.EaseInOutQuad)
        anyPie.legend.isEnabled = false
    }

    private fun setData(anyPie: PieChart, list: List<Reporte>) {
        val entries: ArrayList<PieEntry> = ArrayList()

        val colors: ArrayList<Int> = arrayListOf()
        var total = 0
        list.forEach {
            total += it.cantidad
            if (it.cantidad != 0) {
                entries.add(PieEntry(it.cantidad.toFloat(), it.estado))
                colors.add(Color.parseColor(it.color))
            }
        }

        val dataSet = PieDataSet(entries, "Aviso")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f


        dataSet.colors = colors
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormat(total, anyPie))
        data.setValueTextSize(14f)
        data.setValueTextColor(Color.WHITE)

        anyPie.data = data

        // undo all highlights
        anyPie.setUsePercentValues(true)
        anyPie.highlightValues(null)
        anyPie.invalidate()
    }

    override fun onRefresh() {
        q.userId = usuarioId
        reporteViewModel.clearAviso()
        reporteViewModel.clearInspeccion()
        reporteViewModel.setLoading(true)
        resetChart(anyAvisos)
        resetChart(anyInspeccion)
        reporteViewModel.getReporte(token, q)
    }

    private fun resetChart(anyPie: PieChart) {
        anyPie.notifyDataSetChanged()
        anyPie.clear()
        anyPie.invalidate()
    }
}