package com.amsys.alphamanfacturas.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class ModoFalla {
    @PrimaryKey(autoGenerate = true)
    var modoFallaId: Int = 0
    var codigo: String = ""
    var nombre: String = ""
}