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
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.CompletableObserver
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AvisoViewModel @Inject
internal constructor(private val roomRepository: AppRepository, private val retrofit: ApiError) :
    ViewModel() {

    val mensajeError = MutableLiveData<String>()
    val mensajeSuccess = MutableLiveData<String>()
    val mensajeSync = MutableLiveData<Int>()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val compositeDisposable = CompositeDisposable()
    val paginator = PublishProcessor.create<Int>()
    val pageNumber: MutableLiveData<Int> = MutableLiveData()
    val informacion: MutableLiveData<EquipoInformacion> = MutableLiveData()
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
                    mensajeError.value = e.toString()
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
                                insertEquipos(t.data)
                            }

                            override fun onError(e: Throwable) {
                                mensajeError.value = e.toString()
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

    fun getInformacion(token: String, q: Query) {
        roomRepository.getInformacion(token, q)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ResponseModel> {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                    getModoFalla(token, q)
                }

                override fun onNext(t: ResponseModel) {
                    val gson = Gson().toJson(t.data)
                    Log.i("TAG", gson)
                    val e: EquipoInformacion? = Gson().fromJson(
                        gson, object : TypeToken<EquipoInformacion>() {}.type
                    )
                    if (e != null) {
                        informacion.value = e
                    } else {
                        informacion.value = null
                    }
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
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
                                mensajeError.value = e.toString()
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
                                mensajeError.value = e.toString()
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
                                mensajeError.value = e.toString()
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

    fun sendRegistro(token: String, id: Int) {
        val ots: Observable<Registro> = roomRepository.getRegistroByIdTask(id)
        ots.flatMap { a ->
            val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("tipoAviso", a.tipoAviso.toString())
                .addFormDataPart("consecuenciaId", a.consecuenciaId.toString())
                .addFormDataPart("descripcion", a.descripcion)
                .addFormDataPart("prioridadId", a.prioridadId.toString())
                .addFormDataPart("ubicacionTecnicaId", a.ubicacionTecnicaId.toString())
                .addFormDataPart("emplazamientoId", a.emplazamientoId.toString())
                .addFormDataPart("equipoSuperiorId", a.equipoSuperiorId.toString())
                .addFormDataPart("componenteId", a.componenteId.toString())
                .addFormDataPart("equipoId", a.equipoId.toString())
                .addFormDataPart("areaId", a.areaId.toString())
                .addFormDataPart("sistemaId", a.sistemaId.toString())
                .addFormDataPart("parteId", a.parteId.toString())
                .addFormDataPart("modoFallaOrigenId", a.modoFallaOrigenId.toString())
                .addFormDataPart("metodoDeteccionOrigenId", a.metodoDeteccionOrigenId.toString())
                .addFormDataPart("ordenTrabajoId", a.ordenTrabajoId.toString())
                .addFormDataPart("fecha", a.fecha)
                .addFormDataPart("comentarioRegistro", a.comentarioRegistro)
                .addFormDataPart("inicioParada", a.inicioParada)
                .addFormDataPart("finParada", a.finParada)
                .addFormDataPart("claseParadaId", a.claseParadaId.toString())
                .addFormDataPart("tipoParadaId", a.tipoParadaId.toString())
                .addFormDataPart("subTipoParadaId", a.subTipoParadaId.toString())
                .addFormDataPart("modoFallaId", a.modoFallaId.toString())
                .addFormDataPart("metodoDeteccionId", a.metodoDeteccionId.toString())
                .addFormDataPart("mecanismoFallaId", a.mecanismoFallaId.toString())
                .addFormDataPart("impactoId", a.impactoId.toString())
                .addFormDataPart("causaId", a.causaId.toString())
                .addFormDataPart("comentario", a.comentario)
                .addFormDataPart("userId", a.userId.toString())
                .addFormDataPart("plantaId", a.plantaId.toString())
                .build()
            Observable.zip(
                Observable.just(a),
                roomRepository.sendRegistro(token, body), { _, m -> m })
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ResponseModel> {
                override fun onSubscribe(d: Disposable) {}
                override fun onComplete() {
                }

                override fun onNext(t: ResponseModel) {
                    if (t.response.codigo == "0000") {
                        mensajeSuccess.value = t.response.descripcion
                    } else {
                        mensajeError.value = "${t.response.descripcion} \n${t.response.comentario}"
                    }
                }

                override fun onError(t: Throwable) {
                    mensajeError.value = t.toString()
                }
            })
    }

    private fun updateRegistro() {

    }


}