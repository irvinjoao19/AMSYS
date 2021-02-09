package com.amsys.alphamanfacturas.ui.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.amsys.alphamanfacturas.R
import com.amsys.alphamanfacturas.data.local.model.PuntoMedida
import com.amsys.alphamanfacturas.data.viewModel.InspeccionViewModel
import com.amsys.alphamanfacturas.data.viewModel.ViewModelFactory
import com.amsys.alphamanfacturas.ui.adapters.PuntoMedidaAdapter
import com.amsys.alphamanfacturas.ui.listeners.OnItemClickListener
import com.google.android.material.textfield.TextInputEditText
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_inspeccion_1.*
import java.util.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Inspeccion1Fragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var inspeccionViewModel: InspeccionViewModel
    lateinit var puntoMedidaAdapter: PuntoMedidaAdapter
    private var inspeccionId: Int = 0
    private var usuarioId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            inspeccionId = it.getInt(ARG_PARAM1)
            usuarioId = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inspeccion_1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        inspeccionViewModel =
            ViewModelProvider(this, viewModelFactory).get(InspeccionViewModel::class.java)

        puntoMedidaAdapter =
            PuntoMedidaAdapter(object : OnItemClickListener.PuntoMedidaListener {
                override fun onItemClick(p: PuntoMedida, v: View, position: Int) {
                    when (v.id) {
                        R.id.editText1 -> {
                            val input: TextInputEditText = v.findViewById(R.id.editText1)
                            dialogFecha(p, input)
                        }
                        R.id.textView7 -> {
                            p.fechaMuestra = ""
                            inspeccionViewModel.updatePuntoMedida(p)
                        }
                        R.id.textView9 -> {
                            p.valor =  ""
                            inspeccionViewModel.updatePuntoMedida(p)
                        }
                        R.id.textView11 -> {
                            p.comentario =  ""
                            inspeccionViewModel.updatePuntoMedida(p)
                        }
                    }
                }

                override fun onEditorAction(
                    p: PuntoMedida, t: TextView, p1: Int, p2: KeyEvent?
                ): Boolean {
                    when (t.id) {
                        R.id.editText2 -> {
                            if (t.text.isNotEmpty()) {
                                p.valor = t.text.toString()
                                inspeccionViewModel.updatePuntoMedida(p)
                            }
                        }
                        R.id.editText3 -> {
                            if (t.text.isNotEmpty()) {
                                p.comentario = t.text.toString()
                                inspeccionViewModel.updatePuntoMedida(p)
                            }
                        }
                    }
                    return false
                }
            })

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = puntoMedidaAdapter

        inspeccionViewModel.getPuntoMedidaById(inspeccionId).observe(viewLifecycleOwner) {
            puntoMedidaAdapter.addItems(it)
        }
    }

    private fun dialogFecha(p: PuntoMedida, input: TextInputEditText) {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
                val month =
                    if (((monthOfYear + 1) / 10) == 0) "0" + (monthOfYear + 1).toString() else (monthOfYear + 1).toString()
                val day =
                    if (((dayOfMonth + 1) / 10) == 0) "0$dayOfMonth" else dayOfMonth.toString()
                val fecha = "$day/$month/$year"
                val d = Calendar.getInstance()
                val mHour = d.get(Calendar.HOUR_OF_DAY)
                val mMinute = d.get(Calendar.MINUTE)
                val timePickerDialog =
                    TimePickerDialog(context, { _, hourOfDay, minute ->
                        val hour = if (hourOfDay < 10) "0$hourOfDay" else hourOfDay.toString()
                        val minutes = if (minute < 10) "0$minute" else minute.toString()
                        val result = String.format("%s %s:%s", fecha, hour, minutes)
                        input.setText(result)
                        p.fechaMuestra = result
                        inspeccionViewModel.updatePuntoMedida(p)
                    }, mHour, mMinute, true)
                timePickerDialog.show()
            }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int) =
            Inspeccion1Fragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}