package com.amsys.alphamanfacturas.data.local.model

open class SyncInspeccion {

    var ejecutado: Boolean = false
    var usuarioId: Int = 0
    var fecha: String = ""
    var cantidad: Double = 0.0

    var puntosMedida: List<PuntoMedida> = ArrayList()
    var contadores: List<Contador> = ArrayList()
    var aspectos: List<Aspecto> = ArrayList()
}