package com.amsys.alphamanfacturas.ui.activities

import android.annotation.SuppressLint
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.amsys.alphamanfacturas.R
import com.amsys.alphamanfacturas.data.local.model.Query
import com.amsys.alphamanfacturas.data.viewModel.InspeccionViewModel
import com.amsys.alphamanfacturas.data.viewModel.ViewModelFactory
import com.amsys.alphamanfacturas.helper.Util
import com.amsys.alphamanfacturas.ui.adapters.ViewPagerAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
                confirmation(token, inspeccionId, usuarioId)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var inspeccionViewModel: InspeccionViewModel
    private var token: String = ""
    private var inspeccionId: Int = 0
    private var usuarioId: Int = 0

    lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_inspeccion)
        val b = intent.extras
        if (b != null) {
            token = b.getString("token")!!
            inspeccionId = b.getInt("id")
            usuarioId = b.getInt("user")
            bindUI(token, inspeccionId, usuarioId)
        }
    }

    private fun bindUI(t: String, id: Int, user: Int) {
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
        generateInspeccion(t, id, user)
        statusViewScroller.statusView.run {
            currentCount = 1
        }

        val formAdapter =
            ViewPagerAdapter.FormInspeccion(supportFragmentManager, 4, id, token, user)
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

        inspeccionViewModel.mensajeSuccess.observe(this) {
            Util.toastMensaje(this, it)
            closeLoad()
            if(it != "Datos Sincronizados"){
                finish()
            }
        }
        inspeccionViewModel.mensajeError.observe(this) {
            closeLoad()
            Util.toastMensaje(this, it)
        }
        inspeccionViewModel.mensajeLogout.observe(this) {
            closeLoad()
            Util.dialogMensajeLogin(this)
        }
    }

    private fun load(title: String) {
        builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AppTheme))
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(this).inflate(R.layout.dialog_login, null)
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

    private fun generateInspeccion(token: String, id: Int, user: Int) {
        val q = Query()
        q.userId = user
        q.inspeccionId = id
        load("Sincronizando...")
        inspeccionViewModel.sync(token, q)
    }

    private fun confirmation(token: String, id: Int, user: Int) {
        MaterialAlertDialogBuilder(ContextThemeWrapper(this, R.style.AppTheme))
            .setTitle("Mensaje")
            .setMessage("Deseas enviar esta inspeccion ?")
            .setPositiveButton("Enviar") { d, _ ->
                load("Enviando...")
                inspeccionViewModel.sendInspeccionFile(token, id, user, this)
                d.dismiss()
            }
            .setNegativeButton("No") { d, _ ->
                d.cancel()
            }.show()
    }
}