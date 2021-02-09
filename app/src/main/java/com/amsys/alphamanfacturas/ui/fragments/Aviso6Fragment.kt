package com.amsys.alphamanfacturas.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.amsys.alphamanfacturas.R
import com.amsys.alphamanfacturas.data.local.model.AvisoFile
import com.amsys.alphamanfacturas.data.local.model.InspeccionFile
import com.amsys.alphamanfacturas.data.viewModel.AvisoViewModel
import com.amsys.alphamanfacturas.data.viewModel.ViewModelFactory
import com.amsys.alphamanfacturas.helper.Permission
import com.amsys.alphamanfacturas.helper.Util
import com.amsys.alphamanfacturas.ui.adapters.AvisoFileAdapter
import com.amsys.alphamanfacturas.ui.adapters.InspeccionFileAdapter
import com.amsys.alphamanfacturas.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_inspeccion_4.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Aviso6Fragment : Fragment(), View.OnClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var avisoViewModel: AvisoViewModel

    private var registroId: Int = 0
    private var usuarioId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            registroId = it.getInt(ARG_PARAM1)
            usuarioId = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_aviso_6, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        avisoViewModel =
            ViewModelProvider(this, viewModelFactory).get(AvisoViewModel::class.java)

        val fileAdapter =
            AvisoFileAdapter(object : OnItemClickListener.AvisoFileListener {
                override fun onItemClick(a: AvisoFile, v: View, position: Int) {
                    avisoViewModel.deleteFile(a, requireContext())
                }
            })

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = fileAdapter

        avisoViewModel.getAvisoFiles(registroId).observe(viewLifecycleOwner) {
            fileAdapter.addItems(it)
        }

        avisoViewModel.mensajeSuccess.observe(viewLifecycleOwner) {
            Util.toastMensaje(requireContext(), it)
        }
        avisoViewModel.mensajeError.observe(viewLifecycleOwner) {
            Util.toastMensaje(requireContext(), it)
        }

        btnFiles.setOnClickListener(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int,param2: Int) =
            Aviso6Fragment().apply {
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
                avisoViewModel.getFolderAdjunto(
                    usuarioId,registroId, requireContext(), data
                )
            }
        }
    }
}