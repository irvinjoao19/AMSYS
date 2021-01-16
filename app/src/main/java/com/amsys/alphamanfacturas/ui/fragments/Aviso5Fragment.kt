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
import com.amsys.alphamanfacturas.data.local.model.CausaFalla
import com.amsys.alphamanfacturas.data.local.model.Deteccion
import com.amsys.alphamanfacturas.data.local.model.Impacto
import com.amsys.alphamanfacturas.data.local.model.MecanismoFalla
import com.amsys.alphamanfacturas.data.viewModel.AvisoViewModel
import com.amsys.alphamanfacturas.data.viewModel.ViewModelFactory
import com.amsys.alphamanfacturas.ui.adapters.CausaFallaAdapter
import com.amsys.alphamanfacturas.ui.adapters.DeteccionAdapter
import com.amsys.alphamanfacturas.ui.adapters.ImpactoAdapter
import com.amsys.alphamanfacturas.ui.adapters.MecanismoFallaAdapter
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
            R.id.editText2 -> spinnerDialog(2, "Modo de Falla")
            R.id.editText3 -> spinnerDialog(3,"Método de Detección")
            R.id.editText4 -> spinnerDialog(4, "Impacto en la Función")
            R.id.editText5 -> spinnerDialog(5, "Causa de Falla")
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var avisoViewModel: AvisoViewModel

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
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
//                val cicloAdapter = ComboCicloAdapter(object : OnItemClickListener.CicloListener {
//                    override fun onItemClick(c: Ciclo, view: View, position: Int) {
//                        p.cicloId = c.cicloId
//                        editTextCiclo.setText(c.nombre)
//                        dialog.dismiss()
//                    }
//                })
//                recyclerView.adapter = cicloAdapter
//                itfViewModel.getCicloProceso().observe(this, {
//
//                    cicloAdapter.addItems(it)
//                })
            }
            2 -> {
                layoutSearch.visibility = View.VISIBLE
                val deteccionAdapter =
                    DeteccionAdapter(object : OnItemClickListener.DeteccionListener {
                        override fun onItemClick(d: Deteccion, v: View, position: Int) {
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
        fun newInstance(param1: String, param2: String) =
            Aviso5Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}