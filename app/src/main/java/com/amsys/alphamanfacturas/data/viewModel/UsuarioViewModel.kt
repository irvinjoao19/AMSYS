package com.amsys.alphamanfacturas.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amsys.alphamanfacturas.data.local.model.Query
import com.amsys.alphamanfacturas.data.local.model.ResponseModel
import com.amsys.alphamanfacturas.data.local.model.Usuario
import com.amsys.alphamanfacturas.data.local.repository.ApiError
import com.amsys.alphamanfacturas.data.local.repository.AppRepository
import io.reactivex.CompletableObserver
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UsuarioViewModel @Inject
internal constructor(private val roomRepository: AppRepository, private val retrofit: ApiError) :
    ViewModel() {

    val mensajeError = MutableLiveData<String>()
    val mensajeSuccess = MutableLiveData<String>()

    val user: LiveData<Usuario>
        get() = roomRepository.getUsuario()

    fun setError(s: String) {
        mensajeError.value = s
    }

    fun validateLogin(q: Query) {
        if (q.User.isEmpty()) {
            mensajeError.value = "Ingrese usuario."
            return
        }

        if (q.Password.isEmpty()) {
            mensajeError.value = "Ingrese contraseña."
            return
        }
        getLogin(q)
    }

    private fun getLogin(q: Query) {
        roomRepository.getUsuarioService(q)
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ResponseModel> {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(t: Throwable) {}
                override fun onComplete() {}
                override fun onNext(t: ResponseModel) {
                    if (t.response.codigo == "0000") {
                        insertUsuario(t)
                    } else {
                        mensajeError.value = t.response.comentario
                    }
                }
            })
    }

    fun insertUsuario(u: ResponseModel) {
        roomRepository.insertUsuario((u.data))
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }

                override fun onComplete() {
                    mensajeSuccess.value = "Ok"
                }
            })
    }

    fun logout() {
        roomRepository.deleteSesion()
            .delay(2, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    mensajeSuccess.value = "Close"
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }
}