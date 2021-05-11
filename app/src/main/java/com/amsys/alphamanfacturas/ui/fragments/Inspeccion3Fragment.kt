package com.amsys.alphamanfacturas.ui.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amsys.alphamanfacturas.R
import com.amsys.alphamanfacturas.data.local.model.Aspecto
import com.amsys.alphamanfacturas.data.local.model.Query
import com.amsys.alphamanfacturas.data.viewModel.InspeccionViewModel
import com.amsys.alphamanfacturas.data.viewModel.ViewModelFactory
import com.amsys.alphamanfacturas.ui.adapters.AspectoAdapter
import com.amsys.alphamanfacturas.ui.adapters.ComboAdapter
import com.amsys.alphamanfacturas.ui.listeners.OnItemClickListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_inspeccion_3.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Inspeccion3Fragment : DaggerFragment() {

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
        return inflater.inflate(R.layout.fragment_inspeccion_3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        inspeccionViewModel =
            ViewModelProvider(this, viewModelFactory).get(InspeccionViewModel::class.java)

        val aspectoAdapter =
            AspectoAdapter(object : OnItemClickListener.AspectoListener {
                override fun onItemClick(a: Aspecto, v: View, position: Int) {
                    when (v.id) {
                        R.id.editText1 -> {
                            val input: TextInputEditText = v.findViewById(R.id.editText1)
                            dialogFecha(a, input)
                        }
                        R.id.editText2 -> {
                            val input: TextInputEditText = v.findViewById(R.id.editText2)
                            spinnerDialog(a, input)
                        }

                        R.id.textView7 -> {
                            a.fechaMuestra = ""
                            inspeccionViewModel.updateAspecto(a)
                        }
                        R.id.textView9 -> {
                            a.valor =  ""
                            inspeccionViewModel.updateAspecto(a)
                        }
                        R.id.textView11 -> {
                            a.comentario =  ""
                            inspeccionViewModel.updateAspecto(a)
                        }
                    }
                }

                override fun onEditorAction(
                    c: Aspecto, t: TextView, p1: Int, p2: KeyEvent?
                ): Boolean {
                    when (t.id) {
                        R.id.editText2 -> {
                            if (t.text.isNotEmpty()) {
                                c.valor = t.text.toString()
                                inspeccionViewModel.updateAspecto(c)
                            }
                        }
                        R.id.editText3 -> {
                            if (t.text.isNotEmpty()) {
                                c.comentario = t.text.toString()
                                inspeccionViewModel.updateAspecto(c)
                            }
                        }
                    }
                    return false
                }
            })

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = aspectoAdapter

        inspeccionViewModel.getAspectoById(inspeccionId).observe(viewLifecycleOwner) {
            aspectoAdapter.addItems(it)
        }
    }

    private fun spinnerDialog(a: Aspecto, input: TextInputEditText) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_combo, null)
        val progressBar: ProgressBar = v.findViewById(R.id.progressBar)
        val textViewTitulo: TextView = v.findViewById(R.id.textViewTitulo)
        val recyclerView: RecyclerView = v.findViewById(R.id.recyclerView)
        val layoutSearch: TextInputLayout = v.findViewById(R.id.layoutSearch)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        progressBar.visibility = View.GONE
        layoutSearch.visibility = View.GONE
        builder.setView(v)
        val dialog = builder.create()
        dialog.show()
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context, DividerItemDecoration.VERTICAL
            )
        )
        textViewTitulo.text = String.format("Tipo de Aspectos")


        val comboAdapter =
            ComboAdapter(object : OnItemClickListener.ComboListener {
                override fun onItemClick(q: Query, v: View, position: Int) {
                    input.setText(q.name)
                    a.valor = q.name
                    inspeccionViewModel.updateAspecto(a)
                    dialog.dismiss()
                }
            })
        recyclerView.adapter = comboAdapter

        var enum = 0
        val lista: ArrayList<Query> = ArrayList()
        a.valores.forEach {
            lista.add(Query(enum++, it))
        }
        comboAdapter.addItems(lista)
    }


    private fun dialogFecha(a: Aspecto, input: TextInputEditText) {
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
                        a.fechaMuestra = result
                        inspeccionViewModel.updateAspecto(a)
                    }, mHour, mMinute, true)
                timePickerDialog.show()
            }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int) =
            Inspeccion3Fragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}