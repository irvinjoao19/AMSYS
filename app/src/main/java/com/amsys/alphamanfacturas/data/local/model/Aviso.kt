package com.amsys.alphamanfacturas.data.local.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
open class Aviso {

    @PrimaryKey
    var avisoId: Int = 0
    var tipo: String = ""
    var codigo: String = ""

    var ubicacionCodigo: String = ""
    var ubicacionNombre: String = ""
    var equipoCodigo: String = ""
    var equipoNombre: String = ""

    var comentarioRegistro: String = ""
    var estado: String = ""
    var fechaRegistro: String = ""
}