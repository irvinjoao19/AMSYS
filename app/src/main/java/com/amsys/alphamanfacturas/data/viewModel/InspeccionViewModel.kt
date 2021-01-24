package com.amsys.alphamanfacturas.data.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amsys.alphamanfacturas.data.local.model.*
import com.amsys.alphamanfacturas.data.local.repository.ApiError
import com.amsys.alphamanfacturas.data.local.repository.AppRepository
import com.amsys.alphamanfacturas.helper.Util
import com.google.gson.Gson
import io.reactivex.CompletableObserver
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class InspeccionViewModel @Inject
internal constructor(private val roomRepository: AppRepository, private val retrofit: ApiError) :
    ViewModel() {

    val mensajeError = MutableLiveData<String>()
    val mensajeSuccess = MutableLiveData<String>()
    val mensajeLogout = MutableLiveData<String>()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val compositeDisposable = CompositeDisposable()
    val paginator = PublishProcessor.create<Int>()
    val pageNumber: MutableLiveData<Int> = MutableLiveData()

    fun setError(s: String) {
        mensajeError.value = s
    }

    fun getPageNumber(number: Int) {
        pageNumber.value = number
    }

    fun setLoading(load: Boolean) {
        loading.value = load
    }

    fun paginationInspeccion(token: String, q: Query) {
        val disposable = paginator
            .onBackpressureDrop()
            .delay(1000, TimeUnit.MILLISECONDS)
            .concatMap { page ->
                q.pageNumber = page
                q.pageSize = 10
                val json = Gson().toJson(q)
                Log.i("TAG", json)
                val body =
                    RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
                roomRepository.paginationInspeccion(token, body)
                    .delay(600, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { s -> s }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ s ->
                insertInspecciones(s.data)
                loading.postValue(false)
            }, {
                logout()
                pageNumber.postValue(1)
            })

        compositeDisposable.add(disposable)
        pageNumber.observeForever { n ->
            paginator.onNext(n)
        }
    }

    private fun insertInspecciones(r: Any) {
        roomRepository.insertInspecciones(r)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onError(e: Throwable) {}
            })
    }

    fun getInspecciones(): LiveData<List<Inspeccion>> {
        return roomRepository.getInspecciones()
    }

    fun clear() {
        compositeDisposable.clear()
    }

    fun next(page: Int) {
        paginator.onNext(page)
    }

    fun sync(token: String, q: Query) {
        roomRepository.getSearchInspeccion(token, q)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ResponseModel> {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onNext(t: ResponseModel) {
                    if (t.response.codigo == "0000") {
                        insertSyncInspeccion(t.data)
                    } else {
                        mensajeError.value = t.response.comentario
                    }
                }

                override fun onError(e: Throwable) {
                    logout()
                }
            })
    }

    private fun insertSyncInspeccion(t: Any) {
        roomRepository.insertSyncInspeccion(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    mensajeSuccess.value = "Datos Sincronizados"
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

    fun getPuntoMedidaById(inspeccionId: Int): LiveData<List<PuntoMedida>> {
        return roomRepository.getPuntoMedidaById(inspeccionId)
    }

    fun getContadorById(inspeccionId: Int): LiveData<List<Contador>> {
        return roomRepository.getContadorById(inspeccionId)
    }

    fun getAspectoById(inspeccionId: Int): LiveData<List<Aspecto>> {
        return roomRepository.getAspectoById(inspeccionId)
    }

    fun updatePuntoMedida(p: PuntoMedida) {
        roomRepository.updatePuntoMedida(p)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
    }

    fun updateContador(c:Contador) {
        roomRepository.updateContador(c)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
    }

    fun updateAspecto(a:Aspecto) {
        roomRepository.updateAspecto(a)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
    }
}