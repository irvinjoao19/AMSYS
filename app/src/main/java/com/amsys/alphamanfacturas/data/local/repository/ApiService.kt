package com.amsys.alphamanfacturas.data.local.repository

import com.amsys.alphamanfacturas.data.local.model.ResponseModel
import io.reactivex.Flowable
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @Headers("Cache-Control: no-cache")
    @POST("usuario/login")
    fun getLogin(@Body query: RequestBody): Observable<ResponseModel>

    @Headers("Cache-Control: no-cache")
    @POST("aviso/listar")
    fun getAvisos(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Flowable<ResponseModel>

    @Headers("Cache-Control: no-cache")
    @POST("aviso/parametros")
    fun getParametros(@Header("Authorization") token: String): Observable<ResponseModel>


    @Headers("Cache-Control: no-cache")
    @POST("aviso/listarequipos")
    fun getEquipos(
        @Header("Authorization") token: String,
        @Body query: RequestBody
    ): Observable<ResponseModel>

    @Headers("Cache-Control: no-cache")
    @POST("aviso/buscarinformacion")
    fun getInformacion(
        @Header("Authorization") token: String, @Body body: RequestBody
    ): Observable<ResponseModel>

    @Headers("Cache-Control: no-cache")
    @POST("aviso/listarmodosfalla")
    fun getModoFalla(
        @Header("Authorization") token: String, @Body body: RequestBody
    ): Observable<ResponseModel>

    @Headers("Cache-Control: no-cache")
    @POST("aviso/listartiposparada")
    fun getTipoParada(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Observable<ResponseModel>

    @Headers("Cache-Control: no-cache")
    @POST("aviso/listarsubtiposparada")
    fun getSubTipoParada(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): Observable<ResponseModel>

}