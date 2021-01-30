package com.amsys.alphamanfacturas.data.local.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.amsys.alphamanfacturas.data.local.model.*
import com.amsys.alphamanfacturas.data.local.AppDataBase
import com.amsys.alphamanfacturas.helper.Util
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.RequestBody

class AppRepoImp(private val apiService: ApiService, private val dataBase: AppDataBase) :
    AppRepository {

    override fun getUsuario(): LiveData<Usuario> {
        return dataBase.usuarioDao().getUsuario()
    }

    override fun getUsuarioService(q: Query): Observable<ResponseModel> {
        val json = Gson().toJson(q)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.getLogin(body)
    }

    override fun getUsuarioId(): Observable<Int> {
        return Observable.create {
            val id = dataBase.usuarioDao().getUsuarioIdTask()
            it.onNext(id)
            it.onComplete()
        }
    }

    override fun insertUsuario(o: Any): Completable {
        return Completable.fromAction {
//            val u = Util.genericCastOrNull<Usuario>(o)
            val gson = Gson().toJson(o)
            Log.i("TAG", gson)
            val u: Usuario = Gson().fromJson(
                gson, object : TypeToken<Usuario>() {}.type
            )
            dataBase.usuarioDao().insertUsuarioTask(u)
        }
    }

    override fun deleteSesion(): Completable {
        return Completable.fromAction {
            dataBase.avisoDao().deleteAll()
            dataBase.causaFallaDao().deleteAll()
            dataBase.consecuenciaDao().deleteAll()
            dataBase.deteccionDao().deleteAll()
            dataBase.equipoDao().deleteAll()
            dataBase.impactoDao().deleteAll()
            dataBase.inspeccionDao().deleteAll()
            dataBase.mecanismoFallaDao().deleteAll()
            dataBase.modoFallaDao().deleteAll()
            dataBase.paradaDao().deleteAll()
            dataBase.prioridadDao().deleteAll()
            dataBase.registroDao().deleteAll()
            dataBase.subTipoParadaDao().deleteAll()
            dataBase.tipoParadaDao().deleteAll()
            dataBase.usuarioDao().deleteAll()
        }
    }

    override fun paginationAviso(token: String, body: RequestBody): Flowable<ResponseModel> {
        return apiService.getAvisos(token, body)
    }

    override fun insertLista(r: Any): Completable {
        return Completable.fromAction {
            val gson = Gson().toJson(r)
            Log.i("TAG", gson)
            val l: Lista = Gson().fromJson(
                gson, object : TypeToken<Lista>() {}.type
            )
            val gson2 = Gson().toJson(l.lista)
            Log.i("TAG", gson2)
            val l2: List<Aviso>? = Gson().fromJson(
                gson2, object : TypeToken<List<Aviso>>() {}.type
            )
            val v1: List<Aviso>? = l2
            if (v1 != null) {
                dataBase.avisoDao().insertAvisoListTask(v1)
            }
        }
    }

    override fun getAvisos(): LiveData<List<Aviso>> {
        return dataBase.avisoDao().getAvisos()
    }

    override fun getParametros(token: String): Observable<ResponseModel> {
        return apiService.getParametros(token)
    }

    override fun insertSync(a: Any): Completable {
        return Completable.fromAction {
            val gson = Gson().toJson(a)
            Log.i("TAG", gson)
            val l: SyncAviso = Gson().fromJson(
                gson, object : TypeToken<SyncAviso>() {}.type
            )
            val v1: List<Consecuencia>? = l.consecuencias
            if (v1 != null) {
                dataBase.consecuenciaDao().insertConsecuenciaListTask(v1)
            }
            val v2: List<Prioridad>? = l.prioridades
            if (v2 != null) {
                dataBase.prioridadDao().insertPrioridadListTask(v2)
            }
            val v3: List<Parada>? = l.clasesParada
            if (v3 != null) {
                dataBase.paradaDao().insertParadaListTask(v3)
            }
            val v4: List<Deteccion>? = l.metodosDeteccion
            if (v4 != null) {
                dataBase.deteccionDao().insertDeteccionListTask(v4)
            }
            val v5: List<MecanismoFalla>? = l.mecanismosFalla
            if (v5 != null) {
                dataBase.mecanismoFallaDao().insertMecanismoFallaListTask(v5)
            }
            val v6: List<Impacto>? = l.impactos
            if (v6 != null) {
                dataBase.impactoDao().insertImpactoListTask(v6)
            }
            val v7: List<CausaFalla>? = l.causasFalla
            if (v7 != null) {
                dataBase.causaFallaDao().insertCausaFallaListTask(v7)
            }
        }
    }

    override fun getDeteccion(): LiveData<List<Deteccion>> {
        return dataBase.deteccionDao().getDeteccion()
    }

    override fun getMecanismoFalla(): LiveData<List<MecanismoFalla>> {
        return dataBase.mecanismoFallaDao().getMecanismoFalla()
    }

    override fun getImpacto(): LiveData<List<Impacto>> {
        return dataBase.impactoDao().getImpacto()
    }

    override fun getCausaFalla(): LiveData<List<CausaFalla>> {
        return dataBase.causaFallaDao().getCausaFalla()
    }

    override fun getPrioridad(): LiveData<List<Prioridad>> {
        return dataBase.prioridadDao().getPrioridad()
    }

    override fun getConsecuencia(): LiveData<List<Consecuencia>> {
        return dataBase.consecuenciaDao().getConsecuencia()
    }

    override fun getParada(): LiveData<List<Parada>> {
        return dataBase.paradaDao().getParada()
    }

    override fun getTipoParada(): LiveData<List<TipoParada>> {
        return dataBase.tipoParadaDao().getTipoParadas()
    }

    override fun getSubTipoParada(): LiveData<List<SubTipoParada>> {
        return dataBase.subTipoParadaDao().getSubTipoParadas()
    }

    override fun getIdentity(): LiveData<Int> {
        return dataBase.registroDao().getIdentity()
    }

    override fun insertAviso(r: Registro): Completable {
        return Completable.fromAction {
            val registro: Registro? = dataBase.registroDao().getRegistroByIdTask(r.registroId)
            if (registro == null) {
                dataBase.registroDao().insertRegistroTask(r)
                return@fromAction
            }
            dataBase.registroDao().updateRegistroTask(r)
        }
    }

    override fun getRegistroById(id: Int): LiveData<Registro> {
        return dataBase.registroDao().getRegistroById(id)
    }

    override fun deleteEquipo(): Completable {
        return Completable.fromAction {
            dataBase.equipoDao().deleteAll()
        }
    }

    override fun getEquipos(token: String, q: Query): Observable<ResponseModel> {
        val json = Gson().toJson(q)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.getEquipos(token, body)
    }

    override fun insertEquipos(t: Any): Completable {
        return Completable.fromAction {
            val gson = Gson().toJson(t)
            Log.i("TAG", gson)
            val e: List<Equipo>? = Gson().fromJson(
                gson, object : TypeToken<List<Equipo>>() {}.type
            )
            if (e != null) {
                dataBase.equipoDao().insertEquipoListTask(e)
            }
        }
    }

    override fun getEquipos(): LiveData<List<Equipo>> {
        return dataBase.equipoDao().getEquipos()
    }

    override fun getInformacion(token: String, q: Query): Observable<ResponseModel> {
        val json = Gson().toJson(q)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.getInformacion(token, body)
    }

    override fun deleteModoFalla(): Completable {
        return Completable.fromAction {
            dataBase.modoFallaDao().deleteAll()
        }
    }

    override fun getModoFalla(token: String, q: Query): Observable<ResponseModel> {
        val json = Gson().toJson(q)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.getModoFalla(token, body)
    }

    override fun insertModoFalla(t: Any): Completable {
        return Completable.fromAction {
            val gson = Gson().toJson(t)
            Log.i("TAG", gson)
            val e: List<ModoFalla>? = Gson().fromJson(
                gson, object : TypeToken<List<ModoFalla>>() {}.type
            )
            if (e != null) {
                dataBase.modoFallaDao().insertModoFallaListTask(e)
            }
        }
    }

    override fun getModoFallas(): LiveData<List<ModoFalla>> {
        return dataBase.modoFallaDao().getModoFallas()
    }

    override fun deleteTipoParada(): Completable {
        return Completable.fromAction {
            dataBase.tipoParadaDao().deleteAll()
        }
    }

    override fun getTipoParada(token: String, q: Query): Observable<ResponseModel> {
        val json = Gson().toJson(q)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.getTipoParada(token, body)
    }

    override fun insertTipoParada(t: Any): Completable {
        return Completable.fromAction {
            val gson = Gson().toJson(t)
            Log.i("TAG", gson)
            val e: List<TipoParada>? = Gson().fromJson(
                gson, object : TypeToken<List<TipoParada>>() {}.type
            )
            if (e != null) {
                dataBase.tipoParadaDao().insertTipoParadaListTask(e)
            }
        }
    }

    override fun deleteSubTipoParada(): Completable {
        return Completable.fromAction {
            dataBase.subTipoParadaDao().deleteAll()
        }
    }

    override fun getSubTipoParada(token: String, q: Query): Observable<ResponseModel> {
        val json = Gson().toJson(q)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.getSubTipoParada(token, body)
    }

    override fun insertSubTipoParada(t: Any): Completable {
        return Completable.fromAction {
            val gson = Gson().toJson(t)
            Log.i("TAG", gson)
            val e: List<SubTipoParada>? = Gson().fromJson(
                gson, object : TypeToken<List<SubTipoParada>>() {}.type
            )
            if (e != null) {
                dataBase.subTipoParadaDao().insertSubTipoParadaListTask(e)
            }
        }
    }

    override fun sendRegistro(token: String, body: RequestBody): Observable<ResponseModel> {
        return apiService.sendRegistro(token, body)
    }

    override fun getRegistroByIdTask(id: Int): Observable<Registro> {
        return Observable.create {
            val r = dataBase.registroDao().getRegistroByIdTask(id)
//            if (r == null){
//                it.onError(Throwable(""))
//                it.onComplete()
//                return@create
//            }
            it.onNext(r)
            it.onComplete()
        }
    }

    override fun paginationInspeccion(token: String, body: RequestBody): Flowable<ResponseModel> {
        return apiService.getInspecciones(token, body)
    }

    override fun insertInspecciones(r: Any): Completable {
        return Completable.fromAction {
            val gson = Gson().toJson(r)
            Log.i("TAG", gson)
            val l: Lista = Gson().fromJson(
                gson, object : TypeToken<Lista>() {}.type
            )
            val gson2 = Gson().toJson(l.lista)
            Log.i("TAG", gson2)
            val l2: List<Inspeccion>? = Gson().fromJson(
                gson2, object : TypeToken<List<Inspeccion>>() {}.type
            )
            val v1: List<Inspeccion>? = l2
            if (v1 != null) {
                dataBase.inspeccionDao().insertInspeccionListTask(v1)
            }
        }
    }

    override fun getInspecciones(): LiveData<List<Inspeccion>> {
        return dataBase.inspeccionDao().getInspeccions()
    }

    override fun getSearchInspeccion(token: String, q: Query): Observable<ResponseModel> {
        val json = Gson().toJson(q)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.getSearchInspeccion(token, body)
    }

    override fun insertSyncInspeccion(t: Any): Completable {
        return Completable.fromAction {
            val gson = Gson().toJson(t)
            Log.i("TAG", gson)
            val l: SyncInspeccion = Gson().fromJson(
                gson, object : TypeToken<SyncInspeccion>() {}.type
            )

            val v1: List<PuntoMedida>? = l.puntosMedida
            if (v1 != null) {
                dataBase.puntoMedidaDao().insertPuntoMedidaListTask(v1)
            }
            val v2: List<Contador>? = l.contadores
            if (v2 != null) {
                dataBase.contadorDao().insertContadorListTask(v2)
            }
            val v3: List<Aspecto>? = l.aspectos
            if (v3 != null) {
                dataBase.aspectoDao().insertAspectoListTask(v3)
            }
        }
    }

    override fun getPuntoMedidaById(inspeccionId: Int): LiveData<List<PuntoMedida>> {
        return dataBase.puntoMedidaDao().getPuntoMedidas(inspeccionId)
    }

    override fun getContadorById(inspeccionId: Int): LiveData<List<Contador>> {
        return dataBase.contadorDao().getContadors(inspeccionId)
    }

    override fun getAspectoById(inspeccionId: Int): LiveData<List<Aspecto>> {
        return dataBase.aspectoDao().getAspectos(inspeccionId)
    }

    override fun updatePuntoMedida(p: PuntoMedida): Completable {
        return Completable.fromAction {
            dataBase.puntoMedidaDao().updatePuntoMedidaTask(p)
        }
    }

    override fun updateContador(c: Contador): Completable {
        return Completable.fromAction {
            dataBase.contadorDao().updateContadorTask(c)
        }
    }

    override fun updateAspecto(a: Aspecto): Completable {
        return Completable.fromAction {
            dataBase.aspectoDao().updateAspectoTask(a)
        }
    }

    override fun insertInspeccionFile(f: InspeccionFile): Completable {
        return Completable.fromAction {
            dataBase.inspeccionFileDao().insertInspeccionFileTask(f)
        }
    }

    override fun getInspeccionFiles(id: Int): LiveData<List<InspeccionFile>> {
        return dataBase.inspeccionFileDao().getInspeccionFiles(id)
    }

    override fun deleteFile(f: InspeccionFile, context: Context): Completable {
        return Completable.fromAction {
            Util.deleteFile(f.url, context)
            dataBase.inspeccionFileDao().deleteInspeccionFileTask(f)
        }
    }

    override fun getInspeccionTaskFile(id: Int): Observable<List<InspeccionFile>> {
        return Observable.create {
            val list = dataBase.inspeccionFileDao().getInspeccionFilesTask(id)
            it.onNext(list)
            it.onComplete()
        }
    }

    override fun getInspeccionData(id: Int): Observable<SyncInspeccion> {
        return Observable.create {
            val sync = SyncInspeccion()
            sync.aspectos = dataBase.aspectoDao().getAspectosTask(id)
            sync.contadores = dataBase.contadorDao().getContadoresTask(id)
            sync.puntosMedida = dataBase.puntoMedidaDao().getPuntoMedidasTask(id)
            it.onNext(sync)
            it.onComplete()
        }
    }

    override fun sendInspeccionFile(token: String, body: RequestBody): Observable<ResponseModel> {
        return apiService.sendInspeccionFile(token, body)
    }

    override fun sendInspeccionData(token: String, body: RequestBody): Observable<ResponseModel> {
        return apiService.sendInspeccion(token, body)
    }

    override fun getReporte(token: String, q: Query): Observable<ResponseModel> {
        val json = Gson().toJson(q)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.getReporteGeneral(token, body)
    }
}