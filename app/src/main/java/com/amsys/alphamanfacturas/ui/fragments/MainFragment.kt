package com.amsys.alphamanfacturas.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.amsys.alphamanfacturas.R
import com.amsys.alphamanfacturas.helper.Util
import com.anychart.anychart.*
import com.anychart.anychart.chart.common.Event
import com.anychart.anychart.chart.common.ListenersInterface
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MainFragment : DaggerFragment() {
    private var token: String = ""
    private var usuarioId : Int = 0

    lateinit var pieAvisos: Pie
    lateinit var pieInspection: Pie
    private val datAvisos = ArrayList<DataEntry>()
    private val datInspection = ArrayList<DataEntry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        pieAvisos = AnyChart.pie()
        pieInspection = AnyChart.pie()

        onClickAvisos()
        onClickInspection()
    }

    private fun onClickAvisos() {
        pieAvisos.setOnClickListener(object : ListenersInterface.OnClickListener() {
            override fun onClick(event: Event) {
                Util.toastMensaje(
                    requireContext(), event.data["x"] +
                            ":" + event.data["value"] + ":" + event.data["Porcentaje"]
                )
            }
        })
        pieAvisos.labels.setPosition("outside")
        pieAvisos.legend
            .setPosition("center-bottom")
            .setItemsLayout(LegendLayout.HORIZONTAL)
            .setAlign(EnumsAlign.CENTER)
        anyAvisos.setChart(pieAvisos)
    }

    private fun onClickInspection() {
        pieInspection.setOnClickListener(object : ListenersInterface.OnClickListener() {
            override fun onClick(event: Event) {
                Util.toastMensaje(
                    requireContext(), event.data["x"] +
                            ":" + event.data["value"] + ":" + event.data["Porcentaje"]
                )
            }
        })
        pieInspection.labels.setPosition("outside")
        pieInspection.legend
            .setPosition("center-bottom")
            .setItemsLayout(LegendLayout.HORIZONTAL)
            .setAlign(EnumsAlign.CENTER)
        anyInspection.setChart(pieInspection)
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
}


