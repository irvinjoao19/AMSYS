package com.amsys.alphamanfacturas.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import androidx.lifecycle.ViewModelProvider
import com.amsys.alphamanfacturas.data.viewModel.UsuarioViewModel
import com.amsys.alphamanfacturas.data.viewModel.ViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class SplashActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var usuarioViewModel: UsuarioViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        usuarioViewModel =
            ViewModelProvider(this, viewModelFactory).get(UsuarioViewModel::class.java)

        SystemClock.sleep(500)
        usuarioViewModel.user.observe(this) {
            if (it == null) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}