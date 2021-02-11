package com.amsys.alphamanfacturas.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class AvisoFile {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var avisoId: Int = 0
    var usuarioId: Int = 0
    var name: String = ""
    var url: String = ""
    var type: String = ""
    var size: Long = 0
    var estado: Int = 0 //1 enviado

}