package com.amsys.alphamanfacturas.ui.activities

//import com.shuhart.stepview.StepView

//import com.ydn.viewpagerwithicons.StateViewPager
//import com.ydn.viewpagerwithicons.StateViewPager.OnIconClickListener

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.amsys.alphamanfacturas.R
import com.amsys.alphamanfacturas.helper.Util
import com.amsys.alphamanfacturas.ui.adapters.TabLayoutAdapter
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_form_aviso.*


class FormAvisoActivity : DaggerAppCompatActivity(), View.OnClickListener {

    override fun onClick(v: View) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_aviso)
        val b = intent.extras
        if (b != null) {
            bindUI(b.getInt("id"), b.getInt("tipo"), b.getString("token")!!, b.getInt("user"))
        }
    }

    @SuppressLint("ResourceType")
    private fun bindUI(id: Int, tipo: Int, token: String, user: Int) {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Nuevo Aviso"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        statusViewScroller.statusView.run {
            currentCount = 1
        }

        val formAdapter = TabLayoutAdapter.Form(supportFragmentManager, 5, id, tipo, token, user)
        viewPager.adapter = formAdapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(p: Int, pos: Float, posp: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageSelected(position: Int) {
                statusViewScroller.scrollToStep(position)
                statusViewScroller.statusView.run {
                    currentCount = position + 1
//                    circleFillColorCurrent = Color.parseColor(getString(R.color.colorPrimary))
                }
//                stepView.go(position, true);
                viewPager.currentItem = position
                Util.hideKeyboard(this@FormAvisoActivity)
            }
        })

        fabRegistrar.setOnClickListener(this)
    }
}