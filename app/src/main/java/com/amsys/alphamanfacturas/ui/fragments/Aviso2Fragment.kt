package com.amsys.alphamanfacturas.ui.fragments

import android.annotation.SuppressLint
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
import com.amsys.alphamanfacturas.data.local.model.Equipo
import com.amsys.alphamanfacturas.data.local.model.Query
import com.amsys.alphamanfacturas.data.local.model.Registro
import com.amsys.alphamanfacturas.data.viewModel.AvisoViewModel
import com.amsys.alphamanfacturas.data.viewModel.ViewModelFactory
import com.amsys.alphamanfacturas.helper.Util
import com.amsys.alphamanfacturas.ui.adapters.ComboAdapter
import com.amsys.alphamanfacturas.ui.adapters.EquipoAdapter
import com.amsys.alphamanfacturas.ui.listeners.OnItemClickListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_aviso_2.*
import javax.inject.Inject


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

class Aviso2Fragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.editText8 -> dialogEquipo()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var avisoViewModel: AvisoViewModel

    private var registroId: Int = 0
    private var token: String = ""
    private var usuarioId: Int = 0

    lateinit var r: Registro

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
        return inflater.inflate(R.layout.fragment_aviso_2, container, false)
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
                editText1.setText(it.ubicacionTecnicaCodigo)
                editText2.setText(it.ubicacionTecnicaNombre)
                editText3.setText(it.emplazamientoCodigo)
                editText4.setText(it.emplazamientoNombre)
                editText5.setText(it.equipoPadreCodigo)
                editText6.setText(it.equipoPadreNombre)
                editText7.setText(it.componenteNombre)
                editText8.setText(it.equipoCodigo)
                editText9.setText(it.equipoNombre)
                editText10.setText(it.areaNombre)
            }
        }

        avisoViewModel.mensajeError.observe(viewLifecycleOwner) {
            Util.toastMensaje(requireContext(), it)
        }

        avisoViewModel.mensajeLogout.observe(viewLifecycleOwner) {
            Util.dialogMensajeLogin(requireActivity())
        }

        editText8.setOnClickListener(this)
    }

    private fun dialogEquipo() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_equipo, null)

        val editTextTipo: TextInputEditText = v.findViewById(R.id.editTextTipo)
        val editTextSearch: TextInputEditText = v.findViewById(R.id.editTextSearch)
        val recyclerView: RecyclerView = v.findViewById(R.id.recyclerView)
        val progressBar: ProgressBar = v.findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        val q = Query()
        q.userId = usuarioId

        editTextTipo.setOnClickListener { spinnerDialog(editTextTipo, q) }
        editTextSearch.setOnEditorActionListener { tv, _, _ ->
            if (q.tipo == 0) {
                Util.toastMensaje(requireContext(), "Seleccione Tipo")
            } else {
                if (tv.text.toString().isNotEmpty()) {
                    if (q.tipo == 1) {
                        q.code = tv.text.toString()
                        q.name = ""
                    } else {
                        q.code = ""
                        q.name = tv.text.toString()
                    }
                    avisoViewModel.searchEquipo(token, q)
                    Util.hideKeyboardFrom(requireContext(), tv)
                }
            }
            false
        }

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(
            DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        )

        val equipoAdapter = EquipoAdapter(object : OnItemClickListener.EquipoListener {
            override fun onItemClick(e: Equipo, v: View, position: Int) {
                clearInformation()
                avisoViewModel.getInformacion(token, Query(usuarioId, e.equipoId),r)
                dialog.dismiss()
            }
        })
        recyclerView.adapter = equipoAdapter

        avisoViewModel.getEquipos().observe(viewLifecycleOwner) {
            equipoAdapter.addItems(it)
        }
    }

    private fun spinnerDialog(input: TextInputEditText, t: Query) {
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
        textViewTitulo.text = String.format("Tipo de Consulta")

        val comboAdapter =
            ComboAdapter(object : OnItemClickListener.ComboListener {
                override fun onItemClick(q: Query, v: View, position: Int) {
                    t.tipo = q.tipo
                    input.setText(q.name)
//                    if (q.tipo == 1) {
//                        Util.editTextMaxLength(input2, 3)
//                    } else {
//                        Util.editTextMaxLength(input2, 4)
//                    }
                    dialog.dismiss()
                }
            })
        recyclerView.adapter = comboAdapter
        comboAdapter.addItems(
            listOf(Query(1, "Codigo"), Query(2, "Nombre"))
        )
    }

    private fun clearInformation() {
        editText1.text = null
        editText2.text = null
        editText3.text = null
        editText4.text = null
        editText5.text = null
        editText6.text = null
        editText7.text = null
        editText8.text = null
        editText9.text = null
        editText10.text = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: String, param3: Int) =
            Aviso2Fragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putInt(ARG_PARAM3, param3)
                }
            }
    }
}