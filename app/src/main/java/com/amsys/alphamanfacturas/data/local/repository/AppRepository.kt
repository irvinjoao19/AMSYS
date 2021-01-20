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
    fun insertLista(r: Any): Completable
    fun getAvisos(): LiveData<List<Aviso>>

    // Parametros
    fun getParametros(token: String): Observable<ResponseModel>
    fun insertSync(a: Any): Completable

    //Combos
    fun getDeteccion(): LiveData<List<Deteccion>>
    fun getMecanismoFalla(): LiveData<List<MecanismoFalla>>
    fun getImpacto(): LiveData<List<Impacto>>
    fun getCausaFalla(): LiveData<List<CausaFalla>>
    fun getPrioridad(): LiveData<List<Prioridad>>
    fun getConsecuencia(): LiveData<List<Consecuencia>>
    fun getParada(): LiveData<List<Parada>>
    fun getTipoParada(): LiveData<List<TipoParada>>
    fun getSubTipoParada(): LiveData<List<SubTipoParada>>

    //Registro
    fun getIdentity(): LiveData<Int>
    fun insertAviso(r: Registro): Completable
    fun getRegistroById(id: Int): LiveData<Registro>

    //Equipo
    fun deleteEquipo(): Completable
    fun getEquipos(token: String, q: Query): Observable<ResponseModel>
    fun insertEquipos(t: Any): Completable
    fun getEquipos(): LiveData<List<Equipo>>
    fun getInformacion(token: String, q: Query): Observable<ResponseModel>

    //Modo Falla
    fun deleteModoFalla(): Completable
    fun getModoFalla(token: String, q: Query): Observable<ResponseModel>
    fun insertModoFalla(t: Any): Completable
    fun getModoFallas(): LiveData<List<ModoFalla>>

    //Parada
    fun deleteTipoParada(): Completable
    fun getTipoParada(token: String, q: Query): Observable<ResponseModel>
    fun insertTipoParada(t: Any): Completable
    fun deleteSubTipoParada(): Completable
    fun getSubTipoParada(token: String, q: Query): Observable<ResponseModel>
    fun insertSubTipoParada(t: Any): Completable

}