package com.amsys.alphamanfacturas.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class PuntoMedida {
    @PrimaryKey(autoGenerate = true)
    var puntoMedidaId: Int = 0
    var inspeccionId: Int = 0
    var equipoCodigo: String = ""
    var equipoNombre: String = ""
    var unidadMedida: String = ""
    var puntoMedida: String = ""
    var nroMuestra: Int = 0
    var fechaMuestra: String = ""
    var valor: String = ""
    var comentario: String = ""
    var orden:Int = 0
}