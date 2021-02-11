package com.amsys.alphamanfacturas.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.amsys.alphamanfacturas.R
import com.amsys.alphamanfacturas.data.viewModel.UsuarioViewModel
import com.amsys.alphamanfacturas.data.viewModel.ViewModelFactory
import com.amsys.alphamanfacturas.helper.Util
import com.amsys.alphamanfacturas.ui.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.logout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                confirmLogout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun confirmLogout() {
        MaterialAlertDialogBuilder(ContextThemeWrapper(this, R.style.AppTheme))
            .setTitle("Cerrar Sesión")
            .setMessage("Estas seguro de salir ?")
            .setPositiveButton("Salir") { d, _ ->
                logout = "on"
                load()
                usuarioViewModel.logout()
                d.dismiss()
            }
            .setNegativeButton("No") { d, _ ->
                d.cancel()
            }.show()
    }


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var usuarioViewModel: UsuarioViewModel
    lateinit var builder: AlertDialog.Builder
    private var dialog: AlertDialog? = null
    var logout: String = "off"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindUI()
    }

    private fun bindUI() {
        usuarioViewModel =
            ViewModelProvider(this, viewModelFactory).get(UsuarioViewModel::class.java)
        usuarioViewModel.user.observe(this, { u ->
            if (u != null) {
                setSupportActionBar(toolbar)
                supportActionBar!!.title = "Principal"

                fragmentByDefault(MainFragment.newInstance("Bearer ${u.token}", u.usuarioId))
                bottomNavigation.setOnNavigationItemSelectedListener(object :
                    BottomNavigationView.OnNavigationItemSelectedListener {
                    override fun onNavigationItemSelected(item: MenuItem): Boolean {
                        when (item.itemId) {
                            R.id.main -> {
                                supportActionBar!!.title = "Principal"
                                changeFragment(
                                    MainFragment.newInstance(
                                        "Bearer ${u.token}",
                                        u.usuarioId
                                    )
                                )
                                return true
                            }
                            R.id.alert -> {
                                supportActionBar!!.title = "Avisos"
                                changeFragment(
                                    AvisosFragment.newInstance(
                                        "Bearer ${u.token}",
                                        u.usuarioId
                                    )
                                )
                                return true
                            }
                            R.id.inspection -> {
                                supportActionBar!!.title = "Inspecciones"
                                changeFragment(
                                    InspeccionFragment.newInstance(
                                        "Bearer ${u.token}",
                                        u.usuarioId
                                    )
                                )
                                return true
                            }
                        }
                        return true
                    }
                })
            } else {
                goLogin()
            }
        })


        usuarioViewModel.mensajeSuccess.observe(this) {
            closeLoad()
            if (it == "Close") {
                val intent = Intent(this@MainActivity, SplashActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                Util.toastMensaje(this, it)
            }
        }
        usuarioViewModel.mensajeError.observe(this) {
            closeLoad()
            Util.snackBarMensaje(window.decorView, it)
        }
    }

    private fun fragmentByDefault(fragment: DaggerFragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.content_frame, fragment)
            .commit()
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.content_frame, fragment)
            .commit()

    }

    private fun load() {
        builder = AlertDialog.Builder(ContextThemeWrapper(this@MainActivity, R.style.AppTheme))
        @SuppressLint("InflateParams") val view =
            LayoutInflater.from(this@MainActivity).inflate(R.layout.dialog_login, null)
        builder.setView(view)
        val textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
        textViewTitle.text = getString(R.string.title_logout)
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

    private fun goLogin() {
        if (logout == "off") {
            val intent = Intent(this, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}