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
        @Body query: RequestBody
    ): Flowable<ResponseModel>

    @Headers("Cache-Control: no-cache")
    @POST("aviso/parametros")
    fun getParametros(@Header("Authorization") token: String): Observable<ResponseModel>
}