package com.amsys.alphamanfacturas.data.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amsys.alphamanfacturas.data.local.model.*
import com.amsys.alphamanfacturas.data.local.repository.ApiError
import com.amsys.alphamanfacturas.data.local.repository.AppRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.CompletableObserver
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ReporteViewModel @Inject
internal constructor(private val roomRepository: AppRepository, private val retrofit: ApiError) :
    ViewModel() {

    val mensajeError = MutableLiveData<String>()
    val mensajeSuccess = MutableLiveData<String>()
    val mensajeLogout = MutableLiveData<String>()
    val avisos: MutableLiveData<List<Reporte>> = MutableLiveData()
    val inspecciones: MutableLiveData<List<Reporte>> = MutableLiveData()
    val loading = MutableLiveData<Boolean>()

    fun setError(s: String) {
        mensajeError.value = s
    }

    fun setLoading(s: Boolean) {
        loading.value = s
    }

    fun clearAviso() {
        avisos.value = null
    }

    fun clearInspeccion() {
        inspecciones.value = null
    }

    fun getReporte(token: String, q: Query) {
        roomRepository.getReporte(token, q)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ResponseModel> {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onNext(t: ResponseModel) {
                    val gson = Gson().toJson(t.data)
                    Log.i("TAG", gson)
                    val e: SyncReporte? = Gson().fromJson(
                        gson, object : TypeToken<SyncReporte>() {}.type
                    )

                    if (e != null) {
                        val v1: List<Reporte>? = e.avisos
                        if (v1 != null) {
                            avisos.value = e.avisos
                        }
                        val v2: List<Reporte>? = e.ordenesTrabajo
                        if (v2 != null) {
                            inspecciones.value = e.ordenesTrabajo
                        }
                    }
                    loading.value = false
                }

                override fun onError(e: Throwable) {
                    logout()
                }
            })
    }

    private fun logout() {
        roomRepository.deleteSesion()
            .delay(2, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    mensajeLogout.value = "Close"
                }
            })
    }
}