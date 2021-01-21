package com.amsys.alphamanfacturas.ui.activities

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.amsys.alphamanfacturas.R
import com.amsys.alphamanfacturas.data.viewModel.InspeccionViewModel
import com.amsys.alphamanfacturas.data.viewModel.ViewModelFactory
import com.amsys.alphamanfacturas.helper.Util
import com.amsys.alphamanfacturas.ui.adapters.ViewPagerAdapter
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_form_aviso.*
import javax.inject.Inject

class FormInspeccionActivity : DaggerAppCompatActivity() {


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.send, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save -> {
//                confirmation(token, id)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var inspeccionViewModel: InspeccionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_inspeccion)
        bindUI()
    }

    private fun bindUI() {
        inspeccionViewModel =
            ViewModelProvider(this, viewModelFactory).get(InspeccionViewModel::class.java)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Realizar Inspección"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val drawable = toolbar.navigationIcon
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable!!.colorFilter = BlendModeColorFilter(
                ContextCompat.getColor(this, R.color.colorBlack),
                BlendMode.SRC_ATOP
            )
        } else {
            @Suppress("DEPRECATION")
            drawable!!.setColorFilter(
                ContextCompat.getColor(this, R.color.colorBlack),
                PorterDuff.Mode.SRC_ATOP
            )
        }
        toolbar.setNavigationOnClickListener { finish() }

        statusViewScroller.statusView.run {
            currentCount = 1
        }

        val formAdapter = ViewPagerAdapter.FormInspeccion(supportFragmentManager, 5, 1, "", 1)
        viewPager.adapter = formAdapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(p: Int, pos: Float, posp: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageSelected(position: Int) {
                statusViewScroller.scrollToStep(position)
                statusViewScroller.statusView.run {
                    currentCount = position + 1
                }
                viewPager.currentItem = position
                Util.hideKeyboard(this@FormInspeccionActivity)
            }
        })
    }
}