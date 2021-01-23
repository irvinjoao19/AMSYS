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
    lateinit var q: Query

    lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        q = Query()
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
        avisoViewModel.getPageNumber(pageNumber)
        q.userId = usuarioId
        avisoViewModel.paginationAviso(token, q)

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
        load()
    }

    private fun load() {
        builder = AlertDialog.Builder(ContextThemeWrapper(requireContext(), R.style.AppTheme))
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_login, null)
        val textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
        builder.setView(view)

        textViewTitle.text = String.format("Sincronizando...")

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
}