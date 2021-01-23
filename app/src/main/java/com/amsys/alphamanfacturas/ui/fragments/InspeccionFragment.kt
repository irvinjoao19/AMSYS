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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amsys.alphamanfacturas.R
import com.amsys.alphamanfacturas.data.local.model.Inspeccion
import com.amsys.alphamanfacturas.data.local.model.Query
import com.amsys.alphamanfacturas.data.viewModel.InspeccionViewModel
import com.amsys.alphamanfacturas.data.viewModel.ViewModelFactory
import com.amsys.alphamanfacturas.helper.Util
import com.amsys.alphamanfacturas.ui.activities.FormInspeccionActivity
import com.amsys.alphamanfacturas.ui.adapters.InspeccionAdapter
import com.amsys.alphamanfacturas.ui.listeners.OnItemClickListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_inspeccion.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class InspeccionFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var inspeccionViewModel: InspeccionViewModel

    private var token: String = ""
    private var usuarioId: Int = 0

    private var loading = false
    private var pageNumber = 1
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0
    private var visibleItemCount: Int = 0
    lateinit var q: Query

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
        return inflater.inflate(R.layout.fragment_inspeccion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        inspeccionViewModel =
            ViewModelProvider(this, viewModelFactory).get(InspeccionViewModel::class.java)

        val inspeccionAdapter = InspeccionAdapter(object : OnItemClickListener.InspeccionListener {
            override fun onItemClick(p: Inspeccion, v: View, position: Int) {
                startActivity(
                    Intent(requireContext(), FormInspeccionActivity::class.java)
                        .putExtra("token", token)
                        .putExtra("user", usuarioId)
                        .putExtra("id", p.inspeccionId)
                )
            }
        })

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = inspeccionAdapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!loading) {
                    if (dy > 0) {
                        visibleItemCount = layoutManager.childCount
                        totalItemCount = layoutManager.itemCount
                        lastVisibleItem = layoutManager.findFirstVisibleItemPosition()

                        if ((visibleItemCount + lastVisibleItem) >= totalItemCount) {
                            inspeccionViewModel.setLoading(true)
                            inspeccionViewModel.getPageNumber(pageNumber++)
                            inspeccionViewModel.next(pageNumber)
                        }
                    }
                }
            }
        })
        inspeccionViewModel.getPageNumber(pageNumber)
        q.userId = usuarioId
        inspeccionViewModel.paginationInspeccion(token, q)

        inspeccionViewModel.getInspecciones().observe(viewLifecycleOwner) {
            inspeccionAdapter.addItems(it)
        }

        inspeccionViewModel.loading.observe(viewLifecycleOwner) {
            loading = it
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }

        inspeccionViewModel.mensajeError.observe(viewLifecycleOwner) {
            Util.toastMensaje(requireContext(), it)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: Int) =
            InspeccionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}