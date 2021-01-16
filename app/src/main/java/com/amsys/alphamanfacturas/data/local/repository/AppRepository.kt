package com.amsys.alphamanfacturas.data.local.repository

import androidx.lifecycle.LiveData
import com.amsys.alphamanfacturas.data.local.model.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import okhttp3.RequestBody
import java.util.*

interface AppRepository {

    fun getUsuario(): LiveData<Usuario>
    fun getUsuarioService(q: Query): Observable<ResponseModel>
    fun getUsuarioId(): Observable<Int>

    fun insertUsuario(o: Any): Completable
    fun deleteSesion(): Completable
    fun deleteSync(): Completable

    // Avisos
    fun paginationAviso(token: String, body: RequestBody): Flowable<ResponseModel>
    fun insertLista(r: Any) : Completable
    fun getAvisos(): LiveData<List<Aviso>>

    // Parametros
    fun getParametros(token : String) : Observable<ResponseModel>
    fun insertSync(a: Any): Completable

    //Combos
    fun getDeteccion(): LiveData<List<Deteccion>>
    fun getMecanismoFalla(): LiveData<List<MecanismoFalla>>
    fun getImpacto(): LiveData<List<Impacto>>
    fun getCausaFalla(): LiveData<List<CausaFalla>>
    fun getPrioridad(): LiveData<List<Prioridad>>
    fun getConsecuencia(): LiveData<List<Consecuencia>>
}