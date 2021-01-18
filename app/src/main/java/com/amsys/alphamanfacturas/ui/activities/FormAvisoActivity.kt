package com.amsys.alphamanfacturas.ui.activities

//import com.shuhart.stepview.StepView

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.amsys.alphamanfacturas.R
import com.amsys.alphamanfacturas.ui.adapters.TabLayoutAdapter
import com.ydn.viewpagerwithicons.StateViewPager.OnIconClickListener
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_form_aviso.*


class FormAvisoActivity : DaggerAppCompatActivity(), View.OnClickListener {

    override fun onClick(v: View) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_aviso)
        bindUI()
    }

    @SuppressLint("ResourceType")
    private fun bindUI() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Nuevo Aviso"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        stepView.adapter = TabLayoutAdapter.Form(supportFragmentManager, 5, 1)

        stepView
            .setOrientation(LinearLayout.HORIZONTAL)
            .setIntermediateIconSize(90, 2)
            .setNumberOfIcons(5) //.setOrientation(LinearLayout.HORIZONTAL)
            //.setGravity(Gravity.RIGHT)
            .setMargins(20, 20, 10, 25)
            .setIconSize(30, 30)
            .setSelectedIconSize(30, 30)
            .setOnIconClickListener { iconNum ->
                stepView.setPage(iconNum, true)
            }
            .setMarginBetweenIcons(30) //.setIntermediateIconSize(100, 3)
            .setShowCheckmarks(false, false, false)
            .setCheckmarkColors(Color.parseColor("#FFA233"), 0, 0)
            .setTitles(
                arrayOf(
                    "General",
                    "Objeto Técnico",
                    "Registro de Evento",
                    "Datos de Parada",
                    "Datos de Contabilidad"
                )
            )
            .setIconColors(Color.parseColor("#e0e0e0"), Color.parseColor("#1AC512"), Color.LTGRAY)
            .setRectangularIcons(false, false, false)
            .setBorderSizes(2, 15, 2)
            .setBorderColors(
                Color.parseColor("#FFA233"),
                Color.parseColor("#FFA233"),
                Color.parseColor("#909090")
            )
            .setTextColors(
                Color.parseColor("#909090"),
                Color.parseColor("#FFA233"),
                Color.parseColor("#909090")
            )
            .setTextStyles(0, Typeface.BOLD, 0)
            .setTextGravities(Gravity.BOTTOM, Gravity.BOTTOM, Gravity.BOTTOM)
            .setTextSizes(12, 12, 12)
            .setTextMargins(10, 10, 10)
            .setIntermediateIconColors(Color.parseColor("#FFA233"), Color.parseColor("#909090"))
            .setIntermediateIconStyles("solid", "dotted")
            .requestLayout()
        fabRegistrar.setOnClickListener(this)
    }
}