package com.amsys.alphamanfacturas.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
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
import androidx.viewpager.widget.ViewPager
import com.amsys.alphamanfacturas.R
import com.amsys.alphamanfacturas.data.local.model.*
import com.amsys.alphamanfacturas.data.viewModel.AvisoViewModel
import com.amsys.alphamanfacturas.data.viewModel.ViewModelFactory
import com.amsys.alphamanfacturas.ui.adapters.*
import com.amsys.alphamanfacturas.ui.listeners.OnItemClickListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.ydn.viewpagerwithicons.StateViewPager
//import com.shuhart.stepview.StepView
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_form_aviso.*
import kotlinx.android.synthetic.main.fragment_aviso_1.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Aviso1Fragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.editText2 -> spinnerDialog(1, "Consecuencias relativas")
            R.id.editText4 -> spinnerDialog(2, "Prioridad")


            R.id.fab1 -> stepView!!.currentItem = 1
        }

    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var avisoViewModel: AvisoViewModel


    private var param1: String? = null
    private var param2: String? = null

    private var stepView: StateViewPager? = null

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
        return inflater.inflate(R.layout.fragment_aviso_1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        stepView = requireActivity().findViewById(R.id.stepView)
        avisoViewModel =
            ViewModelProvider(this, viewModelFactory).get(AvisoViewModel::class.java)


        editText2.setOnClickListener(this)
        editText4.setOnClickListener(this)
        fab1.setOnClickListener(this)
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
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Aviso1Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}