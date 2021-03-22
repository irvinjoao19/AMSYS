package com.amsys.alphamanfacturas.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amsys.alphamanfacturas.R
import com.amsys.alphamanfacturas.data.local.model.Aviso
import com.amsys.alphamanfacturas.data.local.model.Query
import com.amsys.alphamanfacturas.data.viewModel.AvisoViewModel
import com.amsys.alphamanfacturas.data.viewModel.ViewModelFactory
import com.amsys.alphamanfacturas.helper.Util
import com.amsys.alphamanfacturas.ui.activities.FormAvisoActivity
import com.amsys.alphamanfacturas.ui.adapters.AvisoAdapter
import com.amsys.alphamanfacturas.ui.listeners.OnItemClickListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_avisos.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AvisosFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        showPopupMenu(v)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var avisoViewModel: AvisoViewModel

    private var token: String = ""
    private var usuarioId: Int = 0

    private var loading = false
    private var pageNumber = 1
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0
    private var visibleItemCount: Int = 0
    private var registroId: Int = 0

    lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            token = it.getString(ARG_PARAM1)!!
            usuarioId = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_avisos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        avisoViewModel =
            ViewModelProvider(this, viewModelFactory).get(AvisoViewModel::class.java)

        val avisoAdapter = AvisoAdapter(object : OnItemClickListener.AvisoListener {
            override fun onItemClick(a: Aviso, v: View, position: Int) {
                if (a.editable) {
                    dialogFinParada(a)
                } else {
                    avisoViewModel.setError("El aviso no se puede modificar.")
                }
            }
        })

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = avisoAdapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!loading) {
                    if (dy > 0) {
                        visibleItemCount = layoutManager.childCount
                        totalItemCount = layoutManager.itemCount
                        lastVisibleItem = layoutManager.findFirstVisibleItemPosition()

                        if ((visibleItemCount + lastVisibleItem) >= totalItemCount) {
                            avisoViewModel.setLoading(true)
                            avisoViewModel.getPageNumber(pageNumber++)
                            avisoViewModel.next(pageNumber)
                        }
                    }
                }
            }
        })

        avisoViewModel.getAvisos().observe(viewLifecycleOwner) {
            avisoAdapter.addItems(it)
        }

        avisoViewModel.loading.observe(viewLifecycleOwner) {
            loading = it
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }

        avisoViewModel.mensajeError.observe(viewLifecycleOwner) {
            Util.toastMensaje(requireContext(), it)
            closeLoad()
        }
        avisoViewModel.mensajeSuccess.observe(viewLifecycleOwner) {
            closeLoad()
        }
        avisoViewModel.mensajeSync.observe(viewLifecycleOwner) {
            if (it != 0) {
                closeLoad()
                goActivity(it)
            }
        }

        avisoViewModel.getIdentity().observe(viewLifecycleOwner) {
            registroId = if (it == null || it == 0) 1 else it + 1
        }
        fab.setOnClickListener(this)
    }

    @SuppressLint("RestrictedApi")
    private fun showPopupMenu(v: View) {
        val popup = PopupMenu(v.context, v)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.avisos, popup.menu)

        val menuBuilder = popup.menu as MenuBuilder
        menuBuilder.setOptionalIconsVisible(true)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.trabajo -> {
                    generateSync(1)
                    true
                }
                R.id.falla -> {
                    generateSync(2)
                    true
                }
                else -> {
                    generateSync(4)
                    true
                }
            }
        }
        popup.show()
    }

    private fun goActivity(tipo: Int) {
        avisoViewModel.clear()
        startActivity(
            Intent(requireContext(), FormAvisoActivity::class.java)
                .putExtra("id", registroId)
                .putExtra("tipo", tipo)
                .putExtra("token", token)
                .putExtra("user", usuarioId)
        )
    }

    private fun generateSync(tipo: Int) {
        avisoViewModel.sync(token, tipo)
        load("Sincronizando...")
    }

    private fun load(title:String) {
        builder = AlertDialog.Builder(ContextThemeWrapper(requireContext(), R.style.AppTheme))
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_login, null)
        val textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
        builder.setView(view)

        textViewTitle.text = title

        dialog = builder.create()
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    private fun closeLoad() {
        if (dialog != null) {
            if (dialog!!.isShowing) {
                dialog!!.dismiss()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: Int) =
            AvisosFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }

    override fun onStart() {
        super.onStart()
        avisoViewModel.setLoading(true)
        avisoViewModel.getPageNumber(1)
        avisoViewModel.paginationAviso(token, usuarioId)
    }

    private fun dialogFinParada(a: Aviso) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(requireContext(), R.style.AppTheme))
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_fecha, null)
        val editTextFecha: TextInputEditText = view.findViewById(R.id.editTextFecha)
        val btnSi: MaterialButton = view.findViewById(R.id.btnSi)
        val btnNo: MaterialButton = view.findViewById(R.id.btnNo)
        builder.setView(view)
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()

        editTextFecha.setOnClickListener {
            Util.getFullDialog(requireContext(), editTextFecha)
        }
        btnSi.setOnClickListener {
            val fecha = editTextFecha.text.toString()
            if (fecha.isEmpty()) {
                avisoViewModel.setError("Seleccionar fecha")
                return@setOnClickListener
            }
            load("Actualizando..")
            val q = Query()
            q.avisoId = a.avisoId
            q.finParada = fecha
            avisoViewModel.actualizarfecha(token, q)
            dialog.dismiss()
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
        }
    }
}