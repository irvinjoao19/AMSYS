package com.amsys.alphamanfacturas.data.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amsys.alphamanfacturas.data.local.model.*
import com.amsys.alphamanfacturas.data.local.repository.ApiError
import com.amsys.alphamanfacturas.data.local.repository.AppRepository
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.CompletableObserver
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AvisoViewModel @Inject
internal constructor(private val roomRepository: AppRepository, private val retrofit: ApiError) :
    ViewModel() {

    val mensajeError = MutableLiveData<String>()
    val mensajeSuccess = MutableLiveData<String>()
    val mensajeSync = MutableLiveData<String>()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val compositeDisposable = CompositeDisposable()
    val paginator = PublishProcessor.create<Int>()
    val pageNumber: MutableLiveData<Int> = MutableLiveData()
//    val lista: MutableLiveData<List<Aviso>> = MutableLiveData()

    fun setError(s: String) {
        mensajeError.value = s
    }

    fun getPageNumber(number: Int) {
        pageNumber.value = number
    }

    fun setLoading(load: Boolean) {
        loading.value = load
    }

    fun paginationAviso(token: String, q: Query) {
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
                roomRepository.paginationAviso(token, body)
                    .delay(600, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { s -> s }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ s ->
                insertLista(s.data)
                loading.postValue(false)
            }, { t ->
                mensajeError.postValue(t.toString())
                pageNumber.postValue(1)
            })

        compositeDisposable.add(disposable)
        pageNumber.observeForever { n ->
            paginator.onNext(n)
        }
    }

    private fun insertLista(r: Any) {
        roomRepository.insertLista(r)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onError(e: Throwable) {}
            })
    }

    fun getAvisos(): LiveData<List<Aviso>> {
        return roomRepository.getAvisos()
    }

    fun clear() {
        compositeDisposable.clear()
    }

    fun next(page: Int) {
        paginator.onNext(page)
    }

    fun sync(s: String) {
        roomRepository.getParametros(s)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ResponseModel> {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onNext(t: ResponseModel) {
                    insertSync(t.data)
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }

    private fun insertSync(a: Any) {
        roomRepository.insertSync(a)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    mensajeSync.value = "Ok"
                }
            })
    }

    fun getDeteccion(): LiveData<List<Deteccion>> {
        return roomRepository.getDeteccion()
    }

    fun getMecanismoFalla(): LiveData<List<MecanismoFalla>> {
        return roomRepository.getMecanismoFalla()
    }

    fun getImpacto(): LiveData<List<Impacto>> {
        return roomRepository.getImpacto()
    }

    fun getCausaFalla(): LiveData<List<CausaFalla>> {
        return roomRepository.getCausaFalla()
    }

    fun getPrioridad(): LiveData<List<Prioridad>> {
        return roomRepository.getPrioridad()
    }

    fun getConsecuencia(): LiveData<List<Consecuencia>> {
        return roomRepository.getConsecuencia()
    }

    fun validateAviso1(r: Registro) {

        insertAviso(r)
    }

    fun validateAviso2(r: Registro) {

        insertAviso(r)
    }

    fun validateAviso3(r: Registro) {

        insertAviso(r)
    }

    fun validateAviso4(r: Registro) {

        insertAviso(r)
    }

    fun validateAviso5(r: Registro) {

        insertAviso(r)
    }

    private fun insertAviso(r: Registro) {
        roomRepository.insertAviso(r)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    mensajeSync.value = "Datos Guardados"
                }
            })
    }

    fun getRegistroById(id: Int): LiveData<Registro> {
        return roomRepository.getRegistroById(id)
    }

}