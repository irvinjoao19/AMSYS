package com.amsys.alphamanfacturas.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Prioridad {

    @PrimaryKey
    var prioridadId: Int = 0
    var nombre: String = ""
}
