package com.amsys.alphamanfacturas.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Aspecto {

    @PrimaryKey(autoGenerate = true)
    var aspectoId: Int = 0
    var inspeccionId:Int = 0
    var equipoCodigo: String = ""
    var equipoNombre: String = ""
    var aspecto: String = ""
    var nroMuestra: Int = 0
    var fechaMuestra: String = ""
    var valor: String = ""
    var comentario: String = ""
    var tipoAspecto: Int = 0
    var tipoValor: Int = 0
    var valores: Array<String> = emptyArray()
    var orden:Int = 0
    var editable : Boolean = false
}
