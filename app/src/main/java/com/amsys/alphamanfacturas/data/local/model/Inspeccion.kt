package com.amsys.alphamanfacturas.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Inspeccion {
    @PrimaryKey(autoGenerate = true)
    var inspeccionId: Int = 0
    var codigo: String = ""
    var descripcion: String = ""
    var ubicacionCodigo: String = ""
    var ubicacionNombre: String = ""
    var prioridadNombre: String = ""
    var estado: String = ""
    var fechaRegistro: String = ""
    var fechaProgramado: String = ""
}