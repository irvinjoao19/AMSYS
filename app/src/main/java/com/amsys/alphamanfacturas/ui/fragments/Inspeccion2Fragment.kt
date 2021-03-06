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
import com.amsys.alphamanfacturas.data.local.model.Contador
import com.amsys.alphamanfacturas.data.viewModel.InspeccionViewModel
import com.amsys.alphamanfacturas.data.viewModel.ViewModelFactory
import com.amsys.alphamanfacturas.ui.adapters.ContadorAdapter
import com.amsys.alphamanfacturas.ui.listeners.OnItemClickListener
import com.google.android.material.textfield.TextInputEditText
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_inspeccion_2.*
import java.util.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Inspeccion2Fragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var inspeccionViewModel: InspeccionViewModel
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
        return inflater.inflate(R.layout.fragment_inspeccion_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        inspeccionViewModel =
            ViewModelProvider(this, viewModelFactory).get(InspeccionViewModel::class.java)

        val contadorAdapter =
            ContadorAdapter(object : OnItemClickListener.ContadorListener {
                override fun onItemClick(c: Contador, v: View, position: Int) {
                    when (v.id) {
                        R.id.editText1 -> {
                            val input: TextInputEditText = v.findViewById(R.id.editText1)
                            dialogFecha(c, input)
                        }
                        R.id.textView7 -> {
                            c.fechaMuestra = ""
                            inspeccionViewModel.updateContador(c)
                        }
                        R.id.textView9 -> {
                            c.valor =  0.0
                            inspeccionViewModel.updateContador(c)
                        }
                        R.id.textView11 -> {
                            c.comentario =  ""
                            inspeccionViewModel.updateContador(c)
                        }
                    }
                }

                override fun onEditorAction(
                    c: Contador, t: TextView, p1: Int, p2: KeyEvent?
                ): Boolean {
                    when (t.id) {
                        R.id.editText2 -> {
                            if (t.text.isNotEmpty()) {
                                c.valor = t.text.toString().toDouble()
                                inspeccionViewModel.updateContador(c)
                            }
                        }
                        R.id.editText3 -> {
                            if (t.text.isNotEmpty()) {
                                c.comentario = t.text.toString()
                                inspeccionViewModel.updateContador(c)
                            }
                        }
                    }
                    return false
                }
            })

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = contadorAdapter

        inspeccionViewModel.getContadorById(inspeccionId).observe(viewLifecycleOwner) {
            contadorAdapter.addItems(it)
        }
    }

    private fun dialogFecha(n: Contador, input: TextInputEditText) {
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
                        n.fechaMuestra = result
                        inspeccionViewModel.updateContador(n)
                    }, mHour, mMinute, true)
                timePickerDialog.show()
            }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int) =
            Inspeccion2Fragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}