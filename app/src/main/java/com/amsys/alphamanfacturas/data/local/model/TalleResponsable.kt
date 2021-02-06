package com.amsys.alphamanfacturas.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class TalleResponsable {

    @PrimaryKey(autoGenerate = true)
    var tallerResponsableId: Int = 0
    var nombre: String = ""
}