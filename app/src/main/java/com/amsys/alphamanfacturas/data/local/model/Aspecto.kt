package com.amsys.alphamanfacturas.data.local.model

import androidx.room.Entity
import androidx.room.Ignore

//@Entity
open class Aspecto {

    var equipoCodigo: String = ""
    var equipoNombre: String = ""
    var aspecto: String = ""
    var nroMuestra: Int = 0
    var fechaMuestra: String = ""
    var valor: String = ""
    var comentario: String = ""
    var tipoAspecto: Int = 0
    var tipoValor: Int = 0
//    var valores: ArrayList<String> = ArrayList()

}
