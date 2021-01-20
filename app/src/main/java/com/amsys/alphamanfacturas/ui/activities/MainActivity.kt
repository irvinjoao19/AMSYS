package com.amsys.alphamanfacturas.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.amsys.alphamanfacturas.R
import com.amsys.alphamanfacturas.data.viewModel.UsuarioViewModel
import com.amsys.alphamanfacturas.data.viewModel.ViewModelFactory
import com.amsys.alphamanfacturas.ui.fragments.AvisosFragment
import com.amsys.alphamanfacturas.ui.fragments.MainFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var usuarioViewModel: UsuarioViewModel
    var logout: String = "off"

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }

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
                                changeFragment(MainFragment.newInstance("Bearer ${u.token}", u.usuarioId))
                                return true
                            }
                            R.id.alert -> {
                                supportActionBar!!.title = "Avisos"
                                changeFragment(AvisosFragment.newInstance("Bearer ${u.token}", u.usuarioId))
                                return true
                            }
                            R.id.inspection -> {
                                supportActionBar!!.title = "Inspecciones"
                                //changeFragment(PedidoFragment.newInstance(u.localId,u.usuarioId))
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

    private fun goLogin() {
        if (logout == "off") {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}