package com.amsys.alphamanfacturas.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.amsys.alphamanfacturas.R
import com.amsys.alphamanfacturas.data.local.model.InspeccionFile
import com.amsys.alphamanfacturas.data.local.model.PuntoMedida
import com.amsys.alphamanfacturas.data.viewModel.InspeccionViewModel
import com.amsys.alphamanfacturas.data.viewModel.ViewModelFactory
import com.amsys.alphamanfacturas.helper.Permission
import com.amsys.alphamanfacturas.helper.Util
import com.amsys.alphamanfacturas.ui.adapters.InspeccionFileAdapter
import com.amsys.alphamanfacturas.ui.adapters.PuntoMedidaAdapter
import com.amsys.alphamanfacturas.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_inspeccion_1.*
import kotlinx.android.synthetic.main.fragment_inspeccion_4.*
import kotlinx.android.synthetic.main.fragment_inspeccion_4.recyclerView
import kotlinx.android.synthetic.main.fragment_main.view.*
import javax.inject.Inject


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Inspeccion4Fragment : DaggerFragment(), View.OnClickListener {

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
        return inflater.inflate(R.layout.fragment_inspeccion_4, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        inspeccionViewModel =
            ViewModelProvider(this, viewModelFactory).get(InspeccionViewModel::class.java)

        val fileAdapter =
            InspeccionFileAdapter(object : OnItemClickListener.InspeccionFileListener {
                override fun onItemClick(f: InspeccionFile, v: View, position: Int) {
                    inspeccionViewModel.deleteFile(f, requireContext())
                }
            })

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = fileAdapter

        inspeccionViewModel.getInspeccionFiles(inspeccionId).observe(viewLifecycleOwner) {
            fileAdapter.addItems(it)
        }

        inspeccionViewModel.mensajeSuccess.observe(viewLifecycleOwner) {
            Util.toastMensaje(requireContext(), it)
        }
        inspeccionViewModel.mensajeError.observe(viewLifecycleOwner) {
            Util.toastMensaje(requireContext(), it)
        }

        btnFiles.setOnClickListener(this)
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

    override fun onClick(v: View) {
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.type = "*/*"
        i.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        startActivityForResult(i, Permission.GALERY_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Permission.GALERY_REQUEST) {
            if (data != null) {
                inspeccionViewModel.getFolderAdjunto(
                    usuarioId, inspeccionId, requireContext(), data
                )
            }
        }
    }
}