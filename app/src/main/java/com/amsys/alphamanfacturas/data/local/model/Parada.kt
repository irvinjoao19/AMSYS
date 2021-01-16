package com.amsys.alphamanfacturas.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Parada {
    @PrimaryKey
    var claseParadaId: Int = 0
    var nombre: String = ""
}
