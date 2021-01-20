package com.amsys.alphamanfacturas.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class TipoParada {
    @PrimaryKey(autoGenerate = true)
    var tipoParadaId: Int = 0
    var codigo: String = ""
    var nombre: String = ""
}