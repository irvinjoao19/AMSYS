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
import com.amsys.alphamanfacturas.data.local.model.Response
import com.amsys.alphamanfacturas.data.viewModel.AvisoViewModel
import com.amsys.alphamanfacturas.data.viewModel.ViewModelFactory
import com.amsys.alphamanfacturas.helper.Util
import com.amsys.alphamanfacturas.ui.adapters.ViewPagerAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_form_aviso.*
import javax.inject.Inject

class FormAvisoActivity : DaggerAppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.send, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save -> {
                confirmation(token, id,user)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var avisoViewModel: AvisoViewModel
    lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null

    private var token: String = ""
    private var id: Int = 0
    private var user: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_aviso)
        val b = intent.extras
        if (b != null) {
            token = b.getString("token", "")
            id = b.getInt("id")
            user = b.getInt("user")
            bindUI(b.getInt("id"), b.getInt("tipo"), b.getString("token", ""), b.getInt("user"))
        }
    }

    private fun bindUI(id: Int, tipo: Int, token: String, user: Int) {
        avisoViewModel =
            ViewModelProvider(this, viewModelFactory).get(AvisoViewModel::class.java)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Nuevo Aviso"
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

        val formAdapter =
            ViewPagerAdapter.FormAvisos(supportFragmentManager, 6, id, tipo, token, user)
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
                Util.hideKeyboard(this@FormAvisoActivity)
            }
        })

        avisoViewModel.mensajeSuccess.observe(this) {
            closeLoad()
            Util.toastMensaje(this, it)
            finish()
        }
        avisoViewModel.response.observe(this) {
            if (it != null) {
                closeLoad()
                dialogError(it)
            }
        }
        avisoViewModel.mensajeLogout.observe(this) {
            closeLoad()
            Util.dialogMensajeLogin(this)
        }
    }

    private fun confirmation(token: String, id: Int, user: Int) {
        MaterialAlertDialogBuilder(ContextThemeWrapper(this, R.style.AppTheme))
            .setTitle("Mensaje")
            .setMessage("Deseas enviar registro ?")
            .setPositiveButton("Enviar") { d, _ ->
                load()
                avisoViewModel.sendAvisoFile(token, id, user, this)
                d.dismiss()
            }
            .setNegativeButton("No") { d, _ ->
                d.cancel()
            }.show()
    }

    private fun dialogError(r: Response) {
        val lista = r.comentario.split(",")
        var descripcion = ""
        lista.forEach {
            descripcion += "- $it.\n"
        }
        MaterialAlertDialogBuilder(ContextThemeWrapper(this, R.style.AppTheme))
            .setTitle(r.descripcion)
            .setMessage(descripcion)
            .setPositiveButton("Entendido") { d, _ ->
                d.dismiss()
            }.show()
    }


    private fun load() {
        builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AppTheme))
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(this).inflate(R.layout.dialog_login, null)
        val textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
        builder.setView(view)
        textViewTitle.text = String.format("Enviando...")
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
}