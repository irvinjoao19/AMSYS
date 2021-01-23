package com.amsys.alphamanfacturas.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Contador {
    @PrimaryKey(autoGenerate = true)
    var contadorId: Int = 0
    var equipoCodigo: String = ""
    var equipoNombre: String = ""
    var unidadMedida: String = ""
    var nroMuestra: Int = 0
    var fechaMuestra: String = ""
    var valor: Double = 0.0
    var comentario: String = ""
}
