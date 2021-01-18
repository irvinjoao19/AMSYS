package com.amsys.alphamanfacturas.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amsys.alphamanfacturas.R
import com.amsys.alphamanfacturas.data.local.model.Registro
import com.amsys.alphamanfacturas.data.viewModel.AvisoViewModel
import com.amsys.alphamanfacturas.data.viewModel.ViewModelFactory
import com.ydn.viewpagerwithicons.StateViewPager
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_aviso_2.*
import javax.inject.Inject


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Aviso2Fragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        stepView!!.currentItem = 2
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var avisoViewModel: AvisoViewModel

    private var registroId: Int = 0
    private var param2: String? = null

    private var stepView: StateViewPager? = null
    lateinit var r: Registro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        r = Registro()
        arguments?.let {
            registroId = it.getInt(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
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
        stepView = requireActivity().findViewById(R.id.stepView)


        fab2.setOnClickListener(this)
    }

    private fun formAviso1() {
        r.tipoAviso = 1
        r.consecuenciaIdNombre = editText2.text.toString()
        r.descripcion = editText3.text.toString()
        r.prioridadIdNombre = editText4.text.toString()
        avisoViewModel.validateAviso1(r)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: String) =
            Aviso2Fragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}