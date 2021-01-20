package com.amsys.alphamanfacturas.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class SubTipoParada {
    @PrimaryKey(autoGenerate = true)
    var subTipoParadaId: Int = 0
    var codigo: String = ""
    var nombre: String = ""
}