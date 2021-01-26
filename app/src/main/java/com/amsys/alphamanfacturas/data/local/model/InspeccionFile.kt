package com.amsys.alphamanfacturas.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class InspeccionFile {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var inspeccionId: Int = 0
    var usuarioId: Int = 0
    var name: String = ""
    var url: String = ""
    var type: String = ""
    var size: Long = 0

}