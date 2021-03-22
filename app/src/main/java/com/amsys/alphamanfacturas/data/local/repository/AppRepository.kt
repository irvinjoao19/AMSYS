package com.amsys.alphamanfacturas.data.local.repository

import android.content.Context
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

    // Avisos
    fun paginationAviso(token: String, body: RequestBody): Flowable<ResponseModel>
    fun insertLista(r: Any): Completable
    fun getAvisos(): LiveData<List<Aviso>>
    fun getAvisoFiles(registroId: Int): LiveData<List<AvisoFile>>

    // Parametros
    fun getParametros(token: String): Observable<ResponseModel>
    fun insertSync(a: Any): Completable

    //Combos
    fun getDeteccion(): LiveData<List<Deteccion>>
    fun getMecanismoFalla(): LiveData<List<MecanismoFalla>>
    fun getImpacto(): LiveData<List<Impacto>>
    fun getCausaFalla(): LiveData<List<CausaFalla>>
    fun getPrioridad(): LiveData<List<Prioridad>>
    fun getTalleResponsable(): LiveData<List<TalleResponsable>>
    fun getConsecuencia(): LiveData<List<Consecuencia>>
    fun getParada(): LiveData<List<Parada>>
    fun getTipoParada(): LiveData<List<TipoParada>>
    fun getSubTipoParada(): LiveData<List<SubTipoParada>>

    //Registro
    fun getIdentity(): LiveData<Int>
    fun insertAviso(r: Registro): Completable
    fun getRegistroById(id: Int): LiveData<Registro>
    fun insertAvisoFile(t: AvisoFile): Completable
    fun deleteFile(a: AvisoFile, context: Context): Completable

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

    //Save Registro
    fun sendAvisoFile(token: String, body: RequestBody): Observable<ResponseModel>
    fun sendRegistro(token: String, body: RequestBody): Observable<ResponseModel>
    fun getRegistroByIdTask(id: Int): Observable<Registro>
    fun getAvisoTaskFile(id: Int): Observable<List<AvisoFile>>
    fun insertAdjuntoRegistro(t: Any, id: Int): Completable
    fun closeAvisoFile(id: Int): Completable

    //Inspecciones
    fun paginationInspeccion(token: String, body: RequestBody): Flowable<ResponseModel>
    fun insertInspecciones(r: Any): Completable
    fun getInspecciones(): LiveData<List<Inspeccion>>
    fun getSearchInspeccion(token: String, q: Query): Observable<ResponseModel>
    fun insertSyncInspeccion(t: Any): Completable

    //Form Inspecciones
    fun getPuntoMedidaById(inspeccionId: Int): LiveData<List<PuntoMedida>>
    fun getContadorById(inspeccionId: Int): LiveData<List<Contador>>
    fun getAspectoById(inspeccionId: Int): LiveData<List<Aspecto>>

    fun updatePuntoMedida(p: PuntoMedida): Completable
    fun updateContador(c: Contador): Completable
    fun updateAspecto(a: Aspecto): Completable
    fun insertInspeccionFile(f: InspeccionFile): Completable
    fun getInspeccionFiles(id: Int): LiveData<List<InspeccionFile>>
    fun deleteFile(f: InspeccionFile, context: Context): Completable
    fun getInspeccionTaskFile(id: Int): Observable<List<InspeccionFile>>
    fun getInspeccionData(id: Int): Observable<SyncInspeccion>

    // Save Inspeccion Registro
    fun sendInspeccionFile(token: String, body: RequestBody): Observable<ResponseModel>
    fun sendInspeccionData(token: String,  body: RequestBody): Observable<ResponseModel>

    // Reporte
    fun getReporte(token:String,q:Query) : Observable<ResponseModel>
    fun actualizarFecha(token:String,q: Query) : Observable<ResponseModel>
    fun updateFechaFinParadaAviso(q: Query): Completable
}