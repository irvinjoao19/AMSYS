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
import com.amsys.alphamanfacturas.ui.adapters.*
import com.amsys.alphamanfacturas.ui.listeners.OnItemClickListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_aviso_5.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Aviso5Fragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.editText1 -> spinnerDialog(1, "Modo de Falla")
            R.id.editText2 -> spinnerDialog(2, "Método de Detección")
            R.id.editText3 -> spinnerDialog(3, "Mecanismo de Falla")
            R.id.editText4 -> spinnerDialog(4, "Impacto en la Función")
            R.id.editText5 -> spinnerDialog(5, "Causa de Falla")
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var avisoViewModel: AvisoViewModel
    private lateinit var r: Registro
    private var registroId: Int = 0
    private var textChange: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        r = Registro()
        arguments?.let {
            registroId = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_aviso_5, container, false)
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
                editText1.setText(it.modoFallaNombre)
                editText2.setText(it.metodoDeteccionNombre)
                editText3.setText(it.mecanismoFallaNombre)
                editText4.setText(it.impactoNombre)
                editText5.setText(it.causaNombre)
                if (textChange) {
                    editText6.setText(it.comentario)
                }
            }
        }

        editText1.setOnClickListener(this)
        editText2.setOnClickListener(this)
        editText3.setOnClickListener(this)
        editText4.setOnClickListener(this)
        editText5.setOnClickListener(this)
        editText6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                textChange = false
                r.comentario = editable.toString()
                avisoViewModel.insertAviso(r)
            }
        })
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
                layoutSearch.visibility = View.GONE
                val modoFallaAdapter =
                    ModoFallaAdapter(object : OnItemClickListener.ModoFallaListener {
                        override fun onItemClick(m: ModoFalla, v: View, position: Int) {
                            editText1.setText(m.nombre)
                            r.modoFallaId = m.modoFallaId
                            r.modoFallaNombre = m.nombre
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
                layoutSearch.visibility = View.VISIBLE
                val deteccionAdapter =
                    DeteccionAdapter(object : OnItemClickListener.DeteccionListener {
                        override fun onItemClick(d: Deteccion, v: View, position: Int) {
                            editText2.setText(d.nombre)
                            r.metodoDeteccionId = d.metodoDeteccionId
                            r.metodoDeteccionNombre = d.nombre
                            avisoViewModel.insertAviso(r)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = deteccionAdapter
                avisoViewModel.getDeteccion().observe(viewLifecycleOwner) {
                    deteccionAdapter.addItems(it)
                }
                editTextSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun onTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun afterTextChanged(editable: Editable) {
                        deteccionAdapter.getFilter().filter(editable)
                    }
                })
            }
            3 -> {
                layoutSearch.visibility = View.VISIBLE
                val mecanismoAdapter =
                    MecanismoFallaAdapter(object : OnItemClickListener.MecanismoFallaListener {
                        override fun onItemClick(m: MecanismoFalla, v: View, position: Int) {
                            editText3.setText(m.nombre)
                            r.mecanismoFallaId = m.mecanismoFallaId
                            r.mecanismoFallaNombre = m.nombre
                            avisoViewModel.insertAviso(r)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = mecanismoAdapter
                avisoViewModel.getMecanismoFalla().observe(this) {
                    mecanismoAdapter.addItems(it)
                }
                editTextSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun onTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun afterTextChanged(editable: Editable) {
                        mecanismoAdapter.getFilter().filter(editable)
                    }
                })
            }
            4 -> {
                layoutSearch.visibility = View.VISIBLE
                val impactoAdapter =
                    ImpactoAdapter(object : OnItemClickListener.ImpactoListener {
                        override fun onItemClick(m: Impacto, v: View, position: Int) {
                            editText4.setText(m.nombre)
                            r.impactoId = m.impactoId
                            r.impactoNombre = m.nombre
                            avisoViewModel.insertAviso(r)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = impactoAdapter
                avisoViewModel.getImpacto().observe(this) {
                    impactoAdapter.addItems(it)
                }
                editTextSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun onTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun afterTextChanged(editable: Editable) {
                        impactoAdapter.getFilter().filter(editable)
                    }
                })
            }
            5 -> {
                layoutSearch.visibility = View.VISIBLE
                val causaFallaAdapter =
                    CausaFallaAdapter(object : OnItemClickListener.CausaFallaListener {
                        override fun onItemClick(c: CausaFalla, v: View, position: Int) {
                            editText5.setText(c.nombre)
                            r.causaId = c.causaFallaId
                            r.causaNombre = c.nombre
                            avisoViewModel.insertAviso(r)
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = causaFallaAdapter
                avisoViewModel.getCausaFalla().observe(this) {
                    causaFallaAdapter.addItems(it)
                }
                editTextSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun onTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
                    override fun afterTextChanged(editable: Editable) {
                        causaFallaAdapter.getFilter().filter(editable)
                    }
                })
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int) =
            Aviso5Fragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }
}