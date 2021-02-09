package com.amsys.alphamanfacturas.data.viewModel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amsys.alphamanfacturas.data.local.model.*
import com.amsys.alphamanfacturas.data.local.repository.ApiError
import com.amsys.alphamanfacturas.data.local.repository.AppRepository
import com.amsys.alphamanfacturas.helper.Mensaje
import com.amsys.alphamanfacturas.helper.Util
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.CompletableObserver
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import java.io.File
import okhttp3.MediaType
import okhttp3.MultipartBody
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

    fun updateContador(c: Contador) {
        roomRepository.updateContador(c)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
    }

    fun updateAspecto(a: Aspecto) {
        roomRepository.updateAspecto(a)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
    }

    fun getFolderAdjunto(user: Int, id: Int, context: Context, data: Intent) {
        Util.getFolderInspeccionAdjunto(user, id, context, data)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<InspeccionFile> {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onNext(t: InspeccionFile) {
                    insertInspeccionFile(t)
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }

    private fun insertInspeccionFile(f: InspeccionFile) {
        roomRepository.insertInspeccionFile(f)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
    }

    fun getInspeccionFiles(id: Int): LiveData<List<InspeccionFile>> {
        return roomRepository.getInspeccionFiles(id)
    }

    fun deleteFile(f: InspeccionFile, context: Context) {
        roomRepository.deleteFile(f, context)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
    }

    fun sendInspeccionFile(token: String, id: Int, user: Int, context: Context) {
        val files = roomRepository.getInspeccionTaskFile(id)
        files.flatMap { observable ->
            Observable.fromIterable(observable).flatMap { a ->
                val b = MultipartBody.Builder()
                b.setType(MultipartBody.FORM)
                b.addFormDataPart("userId", user.toString())
                b.addFormDataPart("inspeccionId", id.toString())
                val file = File(Util.getFolder(context), a.url)
                if (file.exists()) {
                    b.addFormDataPart(
                        "foto", file.name,
                        RequestBody.create(
                            MediaType.parse("multipart/form-data"), file
                        )
                    )
                }
                val body = b.build()
                Observable.zip(
                    Observable.just(a), roomRepository.sendInspeccionFile(token, body),
                    { _, mensaje -> mensaje })
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ResponseModel> {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    sendInspeccionData(token, id)
                }

                override fun onNext(t: ResponseModel) {
                    if (t.response.codigo != "0000") {
                        mensajeError.value = "${t.response.descripcion} \n${t.response.comentario}"
                        return
                    }
                }

                override fun onError(t: Throwable) {
                    logout()
                }
            })
    }

    private fun sendInspeccionData(token: String, id: Int) {
        roomRepository.getInspeccionData(id).flatMap { data ->
            val json = Gson().toJson(data)
            Log.i("TAG", json)
            val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
            Observable.zip(
                Observable.just(data),
                roomRepository.sendInspeccionData(token, body), { _, it -> it })
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ResponseModel> {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onNext(t: ResponseModel) {
                    if (t.response.codigo == "0000") {
                        val gson = Gson().toJson(t.data)
                        val e: Mensaje? = Gson().fromJson(
                            gson, object : TypeToken<Mensaje>() {}.type
                        )
                        mensajeSuccess.value = e?.mensaje
                    } else {
                        mensajeError.value = "${t.response.descripcion} \n${t.response.comentario}"
                    }
                }

                override fun onError(t: Throwable) {
                    logout()
                }
            })
    }
}