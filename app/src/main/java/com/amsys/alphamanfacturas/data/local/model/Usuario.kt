package com.amsys.alphamanfacturas.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Usuario {

    @PrimaryKey
    var usuarioId: Int = 0
    var usuario: String = ""
    var token: String = ""
}