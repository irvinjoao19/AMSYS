package com.amsys.alphamanfacturas.ui.fragments

import android.annotation.SuppressLint
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
import com.amsys.alphamanfacturas.data.local.model.*
import com.amsys.alphamanfacturas.data.viewModel.AvisoViewModel
import com.amsys.alphamanfacturas.data.viewModel.ViewModelFactory
import com.amsys.alphamanfacturas.helper.Util
import com.amsys.alphamanfacturas.ui.adapters.*
import com.amsys.alphamanfacturas.ui.listeners.OnItemClickListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_aviso_1.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

class Aviso1Fragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.editText2 -> spinnerDialog(1, "Consecuencias relativas")
            R.id.editText4 -> spinnerDialog(2, "Prioridad")
            R.id.editText5 -> spinnerDialog(3, "Taller Responsable")
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var avisoViewModel: AvisoViewModel

    private var registroId: Int = 0
    private var tipoAviso: Int = 0
    private var usuarioId: Int = 0

    lateinit var r: Registro
    private var textChange: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        r = Registro()
        arguments?.let {
            registroId = it.getInt(ARG_PARAM1)
            tipoAviso = it.getInt(ARG_PARAM2)
            usuarioId = it.getInt(ARG_PARAM3)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_aviso_1, container, false)
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
                editText1.setText(it.tipoAvisoNombre)
                editText2.setText(it.consecuenciaIdNombre)
                if (textChange) {
                    editText3.setText(it.descripcion)
                }
                editText4.setText(it.prioridadIdNombre)
            }
        }

        avisoViewModel.mensajeError.observe(viewLifecycleOwner) {
            Util.toastMensaje(requireContext(), it)
        }
        avisoViewModel.mensajeSuccess.observe(viewLifecycleOwner) {
            Util.toastMensaje(requireContext(), it)
        }

        editText2.setOnClickListener(this)
        editText4.setOnClickListener(this)
        editText3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                textChange = false
                r.descripcion = editable.toString()
                avisoViewModel.insertAviso(r)
            }
        })
        editText5.setOnClickListener(this)
        editText6.setText(String.format("EMITIDO"))
    }

    private fun spinnerDialog(tipo: Int, title: String) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_combo, null)
        val progressBar: ProgressBar = v.findViewById(R.id.progressBar)
        val textViewTitulo: TextView = v.findViewById(R.id.textViewTitulo)
        val recyclerView: RecyclerView = v.findViewById(R.id.recyclerView)
        val layoutSearch: TextInputLayout = v.findViewById(R.id.layoutSearch)
        val editTextSearch: TextInputEditText = v.findViewById(R.id.editTextSearch)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        progressBar.visibility = View.GONE
        layoutSearch.visibility = View.VISIBLE
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
                val consecuenciaAdapter =
                    ConsecuenciaAdapter(object : OnItemClickListener.ConsecuenciaListener {
                        override fun onItemClick(c: Consecuencia, v: View, position: Int) {
                            editText2.setText(c.nombre)
                            r.consecuenciaId = c.consecuenciaId
                            r.consecuenciaIdNombre = c.nombre
                            avisoViewModel.insertAviso(r)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = consecuenciaAdapter
                avisoViewModel.getConsecuencia().observe(viewLifecycleOwner) {
                    consecuenciaAdapter.addItems(it)
                }
                editTextSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun onTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun afterTextChanged(editable: Editable) {
                        consecuenciaAdapter.getFilter().filter(editable)
                    }
                })
            }
            2 -> {
                val prioridadAdapter =
                    PrioridadAdapter(object : OnItemClickListener.PrioridadListener {
                        override fun onItemClick(p: Prioridad, v: View, position: Int) {
                            editText4.setText(p.nombre)
                            r.prioridadId = p.prioridadId
                            r.prioridadIdNombre = p.nombre
                            avisoViewModel.insertAviso(r)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = prioridadAdapter
                avisoViewModel.getPrioridad().observe(viewLifecycleOwner) {
                    prioridadAdapter.addItems(it)
                }
                editTextSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun onTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun afterTextChanged(editable: Editable) {
                        prioridadAdapter.getFilter().filter(editable)
                    }
                })
            }
            3 -> {
                val tallerAdapter =
                    TalleResponsableAdapter(object : OnItemClickListener.TalleResponsableListener {
                        override fun onItemClick(t: TalleResponsable, v: View, position: Int) {
                            editText5.setText(t.nombre)
                            r.tallerResponsableId = t.tallerResponsableId
                            r.tallerResponsableIdNombre = t.nombre
                            avisoViewModel.insertAviso(r)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = tallerAdapter
                avisoViewModel.getTalleResponsable().observe(viewLifecycleOwner) {
                    tallerAdapter.addItems(it)
                }
                editTextSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun onTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun afterTextChanged(editable: Editable) {
                        tallerAdapter.getFilter().filter(editable)
                    }
                })
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int, param3: Int) =
            Aviso1Fragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                    putInt(ARG_PARAM3, param3)
                }
            }
    }
}