package com.amsys.alphamanfacturas.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Deteccion {
    @PrimaryKey
    var metodoDeteccionId: Int = 0
    var nombre: String = ""
}
