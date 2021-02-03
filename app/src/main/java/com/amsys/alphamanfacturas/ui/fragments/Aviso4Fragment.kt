package com.amsys.alphamanfacturas.ui.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
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
import com.amsys.alphamanfacturas.data.local.model.*
import com.amsys.alphamanfacturas.data.viewModel.AvisoViewModel
import com.amsys.alphamanfacturas.data.viewModel.ViewModelFactory
import com.amsys.alphamanfacturas.helper.Util
import com.amsys.alphamanfacturas.ui.adapters.ParadaAdapter
import com.amsys.alphamanfacturas.ui.adapters.SubTipoParadaAdapter
import com.amsys.alphamanfacturas.ui.adapters.TipoParadaAdapter
import com.amsys.alphamanfacturas.ui.listeners.OnItemClickListener
import com.google.android.material.textfield.TextInputLayout
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_aviso_4.*
import java.util.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

class Aviso4Fragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.editText1 -> getFullDialog(requireContext(), 1)
            R.id.editText2 -> getFullDialog(requireContext(), 2)
            R.id.editText3 -> spinnerDialog(1, "Clase de Parada")
            R.id.editText4 -> spinnerDialog(2, "Tipo de Parada")
            R.id.editText5 -> spinnerDialog(3, "Sub Tipo de Parada")
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var avisoViewModel: AvisoViewModel
    lateinit var r: Registro

    private var registroId: Int = 0
    private var token: String = ""
    private var usuarioId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        r = Registro()
        arguments?.let {
            registroId = it.getInt(ARG_PARAM1)
            token = it.getString(ARG_PARAM2)!!
            usuarioId = it.getInt(ARG_PARAM3)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_aviso_4, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        avisoViewModel =
            ViewModelProvider(this, viewModelFactory).get(AvisoViewModel::class.java)

        avisoViewModel.getRegistroById(registroId).observe(viewLifecycleOwner) {
            if (it != null) {
                r = it
                editText1.setText(it.inicioParada)
                editText2.setText(it.finParada)
                editText3.setText(it.claseParadaNombre)
                editText4.setText(it.tipoParadaNombre)
                editText5.setText(it.subTipoParadaNombre)
            }
        }

        avisoViewModel.mensajeLogout.observe(viewLifecycleOwner) {
            Util.dialogMensajeLogin(requireActivity())
        }

        editText1.setOnClickListener(this)
        editText2.setOnClickListener(this)
        editText3.setOnClickListener(this)
        editText4.setOnClickListener(this)
        editText5.setOnClickListener(this)
    }


    private fun spinnerDialog(tipo: Int, title: String) {
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
        textViewTitulo.text = title

        when (tipo) {
            1 -> {
                val paradaAdapter =
                    ParadaAdapter(object : OnItemClickListener.ParadaListener {
                        override fun onItemClick(p: Parada, v: View, position: Int) {
                            editText3.setText(p.nombre)
                            r.claseParadaId = p.claseParadaId
                            r.claseParadaNombre = p.nombre
                            avisoViewModel.insertAviso(r)

                            val q = Query()
                            q.userId = usuarioId
                            q.claseParadaId = p.claseParadaId
                            avisoViewModel.getTipoParada(token, q)

                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = paradaAdapter
                avisoViewModel.getParada().observe(viewLifecycleOwner) {
                    paradaAdapter.addItems(it)
                }
            }
            2 -> {
                val tipoParadaAdapter =
                    TipoParadaAdapter(object : OnItemClickListener.TipoParadaListener {
                        override fun onItemClick(p: TipoParada, v: View, position: Int) {
                            editText4.setText(p.nombre)
                            r.tipoParadaId = p.tipoParadaId
                            r.tipoParadaNombre = p.nombre
                            avisoViewModel.insertAviso(r)

                            val q = Query()
                            q.userId = usuarioId
                            q.tipoParadaId = p.tipoParadaId
                            avisoViewModel.getSubTipoParada(token, q)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = tipoParadaAdapter
                avisoViewModel.getTipoParada().observe(viewLifecycleOwner) {
                    tipoParadaAdapter.addItems(it)
                }
            }
            3 -> {
                val subTipoParadaAdapter =
                    SubTipoParadaAdapter(object : OnItemClickListener.SubTipoParadaListener {
                        override fun onItemClick(p: SubTipoParada, v: View, position: Int) {
                            editText5.setText(p.nombre)
                            r.subTipoParadaId = p.subTipoParadaId
                            r.subTipoParadaNombre = p.nombre
                            avisoViewModel.insertAviso(r)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = subTipoParadaAdapter
                avisoViewModel.getSubTipoParada().observe(viewLifecycleOwner) {
                    subTipoParadaAdapter.addItems(it)
                }
            }
        }
    }

    private fun getFullDialog(context: Context, tipo: Int) {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(context, { _, year, monthOfYear, dayOfMonth ->
            val month =
                if (((monthOfYear + 1) / 10) == 0) "0" + (monthOfYear + 1).toString() else (monthOfYear + 1).toString()
            val day = if (((dayOfMonth + 1) / 10) == 0) "0$dayOfMonth" else dayOfMonth.toString()
            val fecha = "$day/$month/$year"
            val d = Calendar.getInstance()
            val mHour = d.get(Calendar.HOUR_OF_DAY)
            val mMinute = d.get(Calendar.MINUTE)
            val timePickerDialog =
                TimePickerDialog(context, { _, hourOfDay, minute ->
//                    val hourFormat = if (hourOfDay == 12 || hourOfDay == 0) 12 else hourOfDay % 12
                    val hour = if (hourOfDay < 10) "0$hourOfDay" else hourOfDay.toString()
                    val minutes = if (minute < 10) "0$minute" else minute.toString()
                    val result = String.format("%s %s:%s", fecha, hour, minutes)

                    if (tipo == 1) {
                        editText1.setText(result)
                        r.inicioParada = result
                        avisoViewModel.insertAviso(r)
                    } else {
                        editText2.setText(result)
                        r.finParada = result
                        avisoViewModel.insertAviso(r)
                    }

                }, mHour, mMinute, true)
            timePickerDialog.show()



        }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: String, param3: Int) =
            Aviso4Fragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putInt(ARG_PARAM3, param3)
                }
            }
    }
}