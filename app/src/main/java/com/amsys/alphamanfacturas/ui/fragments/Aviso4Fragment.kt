package com.amsys.alphamanfacturas.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amsys.alphamanfacturas.R
import com.ydn.viewpagerwithicons.StateViewPager
//import com.shuhart.stepview.StepView
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_aviso_4.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Aviso4Fragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        stepView!!.currentItem = 4
    }

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
        return inflater.inflate(R.layout.fragment_aviso_4, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        stepView = requireActivity().findViewById(R.id.stepView)


        fab4.setOnClickListener(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Aviso4Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}