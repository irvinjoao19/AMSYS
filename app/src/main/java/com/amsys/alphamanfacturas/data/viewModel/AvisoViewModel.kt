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
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AvisoViewModel @Inject
internal constructor(private val roomRepository: AppRepository, private val retrofit: ApiError) :
    ViewModel() {

    val mensajeError = MutableLiveData<String>()
    val response = MutableLiveData<Response>()
    val mensajeSuccess = MutableLiveData<String>()
    val mensajeSync = MutableLiveData<Int>()
    val mensajeLogout = MutableLiveData<String>()
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

    fun paginationAviso(token: String, usuarioId: Int) {
        val disposable = paginator
            .onBackpressureDrop()
            .delay(1000, TimeUnit.MILLISECONDS)
            .concatMap { page ->
                val q = Query()
                q.userId = usuarioId
                q.pageNumber = page
                q.pageSize = 10
                val json = Gson().toJson(q)
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
            }, {
                logout()
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

    fun sync(s: String, tipo: Int) {
        roomRepository.getParametros(s)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ResponseModel> {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onNext(t: ResponseModel) {
                    insertSync(t.data, tipo)
                }

                override fun onError(e: Throwable) {
                    logout()
                }
            })
    }

    private fun insertSync(a: Any, tipo: Int) {
        roomRepository.insertSync(a)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    mensajeSync.value = tipo
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

    fun insertAviso(r: Registro) {
        roomRepository.insertAviso(r)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
    }

    fun getRegistroById(id: Int): LiveData<Registro> {
        return roomRepository.getRegistroById(id)
    }

    fun searchEquipo(token: String, q: Query) {
        roomRepository.deleteEquipo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    roomRepository.getEquipos(token, q)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<ResponseModel> {
                            override fun onSubscribe(d: Disposable) {}
                            override fun onComplete() {}
                            override fun onNext(t: ResponseModel) {
                                if (t.response.codigo == "0000") {
                                    insertEquipos(t.data)
                                } else {
                                    mensajeError.value = t.response.comentario
                                }
                            }

                            override fun onError(e: Throwable) {
                                logout()
                            }
                        })
                }
            })
    }

    private fun insertEquipos(t: Any) {
        roomRepository.insertEquipos(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
    }

    fun getEquipos(): LiveData<List<Equipo>> {
        return roomRepository.getEquipos()
    }

    fun getInformacion(token: String, q: Query, r: Registro) {
        roomRepository.getInformacion(token, q)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ResponseModel> {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    getModoFalla(token, q)
                }

                override fun onNext(t: ResponseModel) {
                    if (t.response.codigo == "0000") {
                        val gson = Gson().toJson(t.data)
                        Log.i("TAG", gson)
                        val e: EquipoInformacion? = Gson().fromJson(
                            gson, object : TypeToken<EquipoInformacion>() {}.type
                        )
                        if (e != null) {
                            r.ubicacionTecnicaId = e.ubicacionTecnicaId
                            r.ubicacionTecnicaCodigo = e.ubicacionTecnicaCodigo
                            r.ubicacionTecnicaNombre = e.ubicacionTecnicaNombre
                            r.emplazamientoId = e.emplazamientoId
                            r.emplazamientoCodigo = e.emplazamientoCodigo
                            r.emplazamientoNombre = e.emplazamientoNombre
                            r.equipoPadreId = e.equipoPadreId
                            r.equipoPadreCodigo = e.equipoPadreCodigo
                            r.equipoPadreNombre = e.equipoPadreNombre
                            r.componenteId = e.componenteId
                            r.componenteNombre = e.componenteNombre
                            r.equipoId = e.equipoId
                            r.equipoCodigo = e.equipoCodigo
                            r.equipoNombre = e.equipoNombre
                            r.areaId = e.areaId
                            r.areaNombre = e.areaNombre
                            r.plantaId = e.plantaId
                            insertAviso(r)
                        } else {
                            r.ubicacionTecnicaId = 0
                            r.ubicacionTecnicaCodigo = ""
                            r.ubicacionTecnicaNombre = ""
                            r.emplazamientoId = 0
                            r.emplazamientoCodigo = ""
                            r.emplazamientoNombre = ""
                            r.equipoPadreId = 0
                            r.equipoPadreCodigo = ""
                            r.equipoPadreNombre = ""
                            r.componenteId = 0
                            r.componenteNombre = ""
                            r.equipoId = 0
                            r.equipoCodigo = ""
                            r.equipoNombre = ""
                            r.areaId = 0
                            r.areaNombre = ""
                            r.plantaId = 0
                            insertAviso(r)
                        }
                    } else {
                        mensajeError.value = t.response.comentario
                    }
                }

                override fun onError(e: Throwable) {
                    logout()
                }
            })
    }

    fun getModoFalla(token: String, q: Query) {
        roomRepository.deleteModoFalla()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    roomRepository.getModoFalla(token, q)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<ResponseModel> {
                            override fun onSubscribe(d: Disposable) {}
                            override fun onComplete() {}
                            override fun onNext(t: ResponseModel) {
                                insertModoFalla(t.data)
                            }

                            override fun onError(e: Throwable) {
                                logout()
                            }
                        })
                }
            })
    }

    private fun insertModoFalla(t: Any) {
        roomRepository.insertModoFalla(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
    }

    fun getModoFallas(): LiveData<List<ModoFalla>> {
        return roomRepository.getModoFallas()
    }

    fun getParada(): LiveData<List<Parada>> {
        return roomRepository.getParada()
    }

    fun getTipoParada(token: String, q: Query) {
        roomRepository.deleteTipoParada()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    roomRepository.getTipoParada(token, q)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<ResponseModel> {
                            override fun onSubscribe(d: Disposable) {}
                            override fun onComplete() {}
                            override fun onNext(t: ResponseModel) {
                                insertTipoParada(t.data)
                            }

                            override fun onError(e: Throwable) {
                                logout()
                            }
                        })
                }
            })
    }

    private fun insertTipoParada(t: Any) {
        roomRepository.insertTipoParada(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
    }

    fun getTipoParada(): LiveData<List<TipoParada>> {
        return roomRepository.getTipoParada()
    }

    fun getSubTipoParada(token: String, q: Query) {
        roomRepository.deleteSubTipoParada()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    roomRepository.getSubTipoParada(token, q)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<ResponseModel> {
                            override fun onSubscribe(d: Disposable) {}
                            override fun onComplete() {}
                            override fun onNext(t: ResponseModel) {
                                insertSubTipoParada(t.data)
                            }

                            override fun onError(e: Throwable) {
                                logout()
                            }
                        })
                }
            })
    }

    private fun insertSubTipoParada(t: Any) {
        roomRepository.insertSubTipoParada(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
    }

    fun getSubTipoParada(): LiveData<List<SubTipoParada>> {
        return roomRepository.getSubTipoParada()
    }

    fun getIdentity(): LiveData<Int> {
        return roomRepository.getIdentity()
    }

    fun sendAvisoFile(token: String, id: Int, user: Int, context: Context) {
        val files = roomRepository.getAvisoTaskFile(id)
        files.flatMap { observable ->
            Observable.fromIterable(observable).flatMap { a ->
                val b = MultipartBody.Builder()
                b.setType(MultipartBody.FORM)
                b.addFormDataPart("userId", user.toString())
                val file = File(Util.getFolder(context), a.url)
                if (file.exists()) {
                    b.addFormDataPart(
                        "adjunto", file.name,
                        RequestBody.create(
                            MediaType.parse("multipart/form-data"), file
                        )
                    )
                }
                val body = b.build()
                Observable.zip(
                    Observable.just(a), roomRepository.sendAvisoFile(token, body),
                    { _, t ->
                        t
                    })
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ResponseModel> {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    sendRegistro(token, id)
                }

                override fun onNext(t: ResponseModel) {
                    if (t.response.codigo != "0000") {
                        mensajeError.value = "${t.response.descripcion} \n${t.response.comentario}"
                        return
                    } else {
                        insertAdjuntoRegistro(t.data, id)
                    }
                }

                override fun onError(t: Throwable) {
                    logout()
                }
            })
    }

    private fun insertAdjuntoRegistro(t: Any, id: Int) {
        roomRepository.insertAdjuntoRegistro(t, id)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
    }

    private fun sendRegistro(token: String, id: Int) {
        val register: Observable<Registro> = roomRepository.getRegistroByIdTask(id)
        register.flatMap { a ->
            val json = Gson().toJson(a)
            Log.i("TAG", json)
            val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
            Observable.zip(
                Observable.just(a),
                roomRepository.sendRegistro(token, body), { _, m -> m })
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ResponseModel> {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onNext(t: ResponseModel) {
                    closeAvisoFile(id)
                    if (t.response.codigo == "0000") {
                        val gson = Gson().toJson(t.data)
                        val e: Mensaje? = Gson().fromJson(
                            gson, object : TypeToken<Mensaje>() {}.type
                        )
                        mensajeSuccess.value = e?.mensaje
                    } else {
                        response.value = t.response
                    }
                }

                override fun onError(t: Throwable) {
                    logout()
                }
            })
    }

    private fun closeAvisoFile(id: Int) {
        roomRepository.closeAvisoFile(id)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {}
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

    fun getTalleResponsable(): LiveData<List<TalleResponsable>> {
        return roomRepository.getTalleResponsable()
    }

    fun getFolderAdjunto(user: Int, id: Int, context: Context, data: Intent) {
        Util.getFolderAvisoAdjunto(user, id, context, data)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<AvisoFile> {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {}
                override fun onNext(t: AvisoFile) {
                    insertAvisoFile(t)
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }

    private fun insertAvisoFile(t: AvisoFile) {
        roomRepository.insertAvisoFile(t)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
    }

    fun getAvisoFiles(registroId: Int): LiveData<List<AvisoFile>> {
        return roomRepository.getAvisoFiles(registroId)
    }

    fun deleteFile(a: AvisoFile, requireContext: Context) {
        roomRepository.deleteFile(a, requireContext)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
    }
}