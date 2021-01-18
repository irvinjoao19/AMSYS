package com.amsys.alphamanfacturas.data.local.repository

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
import java.util.*

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
//            dataBase.formatoDao().deleteAll()
//            dataBase.photoDao().deleteAll()
//            dataBase.reciboDao().deleteAll()
//            dataBase.registroDao().deleteAll()
//            dataBase.repartoDao().deleteAll()
//            dataBase.servicioDao().deleteAll()
//            dataBase.usuarioDao().deleteAll()
        }
    }

    override fun deleteSync(): Completable {
        return Completable.fromAction {
//            dataBase.formatoDao().deleteAll()
//            dataBase.photoDao().deleteAll()
//            dataBase.reciboDao().deleteAll()
//            dataBase.registroDao().deleteAll()
//            dataBase.repartoDao().deleteAll()
//            dataBase.servicioDao().deleteAll()
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

            val v1: List<Aviso>? = l.lista
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
            val l: Sync = Gson().fromJson(
                gson, object : TypeToken<Sync>() {}.type
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
            val v5: List<MecanismoFalla>? = l.mecanismoFalla
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
}