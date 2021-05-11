package com.amsys.alphamanfacturas.ui.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.ViewModelProvider
import com.amsys.alphamanfacturas.R
import com.amsys.alphamanfacturas.data.local.model.Ejecucion
import com.amsys.alphamanfacturas.data.viewModel.InspeccionViewModel
import com.amsys.alphamanfacturas.data.viewModel.ViewModelFactory
import com.amsys.alphamanfacturas.helper.Util
import com.google.android.material.textfield.TextInputEditText
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_inspeccion_4.*
import java.util.*
import javax.inject.Inject


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Inspeccion4Fragment : DaggerFragment(), View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.editTextFecha -> getDateDialog(requireContext(), editTextFecha)
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var inspeccionViewModel: InspeccionViewModel
    private lateinit var e: Ejecucion
    private var inspeccionId: Int = 0
    private var usuarioId: Int = 0
    private var textChange: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        e = Ejecucion()
        arguments?.let {
            inspeccionId = it.getInt(ARG_PARAM1)
            usuarioId = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inspeccion_4, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        inspeccionViewModel =
            ViewModelProvider(this, viewModelFactory).get(InspeccionViewModel::class.java)

        e.inspeccionId = inspeccionId
        inspeccionViewModel.getEjecucionById(inspeccionId).observe(viewLifecycleOwner, {
            if (it != null) {
                e = it
                swtEjecucion.isChecked = it.ejecutado
                editTextFecha.setText(it.fecha)
                if (textChange) {
                    if (it.cantidad != 0.0)
                        editTextTiempoTrabajo.setText(it.cantidad.toString())
                }
            }
        })

        editTextFecha.setText(Util.getFecha())
        editTextFecha.setOnClickListener(this)
        swtEjecucion.setOnCheckedChangeListener(this)
        editTextTiempoTrabajo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(s: Editable) {
                textChange = false
                var text: String = s.toString()
                if (text.contains(".")) {
                    val index = text.indexOf(".")
                    if (index + 3 < text.length) {
                        text = text.substring(0, index + 3)
                        editTextTiempoTrabajo.setText(text)
                        editTextTiempoTrabajo.setSelection(text.length)
                        when {
                            s.toString().isEmpty() -> e.cantidad = 0.0
                            else -> e.cantidad = text.toDouble()
                        }
                        if (e.fecha.isNotEmpty()) {
                            inspeccionViewModel.insertEjecucion(e)
                        }
                    }
                }
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int) =
            Inspeccion4Fragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }

    private fun getDateDialog(context: Context, input: TextInputEditText) {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(context, { _, year, monthOfYear, dayOfMonth ->
            val month =
                if (((monthOfYear + 1) / 10) == 0) "0" + (monthOfYear + 1).toString() else (monthOfYear + 1).toString()
            val day = String.format("%02d", dayOfMonth)
            val fecha = "$day/$month/$year"
            input.setText(fecha)
            e.fecha = fecha
            inspeccionViewModel.insertEjecucion(e)

        }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        when (buttonView.id) {
            R.id.swtEjecucion -> {
                if (buttonView.isPressed) {
                    swtEjecucion.isChecked = isChecked
                    e.ejecutado = isChecked
                    inspeccionViewModel.insertEjecucion(e)
                }
            }
        }
    }
}