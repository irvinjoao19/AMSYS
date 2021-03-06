package com.amsys.alphamanfacturas.ui.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.amsys.alphamanfacturas.data.local.model.Deteccion
import com.amsys.alphamanfacturas.data.local.model.ModoFalla
import com.amsys.alphamanfacturas.data.local.model.Registro
import com.amsys.alphamanfacturas.data.viewModel.AvisoViewModel
import com.amsys.alphamanfacturas.data.viewModel.ViewModelFactory
import com.amsys.alphamanfacturas.ui.adapters.DeteccionAdapter
import com.amsys.alphamanfacturas.ui.adapters.ModoFallaAdapter
import com.amsys.alphamanfacturas.ui.listeners.OnItemClickListener
import com.google.android.material.textfield.TextInputLayout
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_aviso_3.*
import java.util.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Aviso3Fragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.editText1 -> spinnerDialog(1, "Modo Falla")
            R.id.editText2 -> spinnerDialog(2, "Método de Detección")
            R.id.editText4 -> dialogFecha()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var avisoViewModel: AvisoViewModel

    private var registroId: Int = 0
    private var tipoAviso: Int = 0
    private var textChange: Boolean = true
    lateinit var r: Registro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        r = Registro()
        arguments?.let {
            registroId = it.getInt(ARG_PARAM1)
            tipoAviso = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_aviso_3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        avisoViewModel =
            ViewModelProvider(this, viewModelFactory).get(AvisoViewModel::class.java)

        if (tipoAviso == 4) {
            txt1.visibility = View.GONE
            layout1.visibility = View.GONE
            txt2.visibility = View.GONE
            layout2.visibility = View.GONE
            txt4.visibility = View.GONE
            layout4.visibility = View.GONE

            txtr2.visibility = View.GONE
            txtr3.visibility = View.GONE
        }

        if (tipoAviso == 1){
            txtr2.visibility = View.GONE
            txtr3.visibility = View.GONE
        }

        avisoViewModel.getRegistroById(registroId).observe(viewLifecycleOwner) {
            if (it != null) {
                r = it
                editText1.setText(it.modoFallaOriginNombre)
                editText2.setText(it.metodoDeteccionOrigenNombre)
                editText4.setText(it.fecha)
                if (textChange) {
                    editText5.setText(it.comentarioRegistro)
                }
            }
        }

        editText1.setOnClickListener(this)
        editText2.setOnClickListener(this)
        editText4.setOnClickListener(this)
        editText5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                textChange = false
                r.comentarioRegistro = editable.toString()
                avisoViewModel.insertAviso(r)
            }
        })
//        fab3.setOnClickListener(this)
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
                val modoFallaAdapter =
                    ModoFallaAdapter(object : OnItemClickListener.ModoFallaListener {
                        override fun onItemClick(m: ModoFalla, v: View, position: Int) {
                            editText1.setText(m.nombre)
                            r.modoFallaId = m.modoFallaId
                            r.modoFallaOriginNombre = m.nombre
                            avisoViewModel.insertAviso(r)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = modoFallaAdapter
                avisoViewModel.getModoFallas().observe(viewLifecycleOwner) {
                    modoFallaAdapter.addItems(it)
                }
            }
            2 -> {
                val deteccionAdapter =
                    DeteccionAdapter(object : OnItemClickListener.DeteccionListener {
                        override fun onItemClick(d: Deteccion, v: View, position: Int) {
                            editText2.setText(d.nombre)
                            r.metodoDeteccionOrigenId = d.metodoDeteccionId
                            r.metodoDeteccionOrigenNombre = d.nombre
                            avisoViewModel.insertAviso(r)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = deteccionAdapter
                avisoViewModel.getDeteccion().observe(viewLifecycleOwner) {
                    deteccionAdapter.addItems(it)
                }
            }
        }
    }

    private fun dialogFecha() {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
                val month =
                    if (((monthOfYear + 1) / 10) == 0) "0${monthOfYear + 1}" else "${monthOfYear + 1}"
                val day = String.format("%02d", dayOfMonth)
                val fecha = "$day/$month/$year"
                editText4.setText(fecha)
                r.fecha = fecha
                avisoViewModel.insertAviso(r)
            }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int) =
            Aviso3Fragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}