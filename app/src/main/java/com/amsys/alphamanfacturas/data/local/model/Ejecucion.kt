package com.amsys.alphamanfacturas.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Ejecucion {
    @PrimaryKey
    var inspeccionId: Int = 0
    var ejecutado: Boolean = false
    var usuarioId: Int = 0
    var fecha: String = ""
    var cantidad: Double = 0.0
}