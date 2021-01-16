package com.amsys.alphamanfacturas.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Consecuencia {

    @PrimaryKey
    var consecuenciaId: Int = 0
    var nombre: String = ""
}
