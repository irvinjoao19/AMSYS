package com.amsys.alphamanfacturas.ui.fragments

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
import com.amsys.alphamanfacturas.data.local.model.Aspecto
import com.amsys.alphamanfacturas.data.viewModel.InspeccionViewModel
import com.amsys.alphamanfacturas.data.viewModel.ViewModelFactory
import com.amsys.alphamanfacturas.ui.adapters.AspectoAdapter
import com.amsys.alphamanfacturas.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_inspeccion_3.*
import javax.inject.Inject

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

                }

                override fun onEditorAction(
                    c: Aspecto, t: TextView, p1: Int, p2: KeyEvent
                ): Boolean {

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