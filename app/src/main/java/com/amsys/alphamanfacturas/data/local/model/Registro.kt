package com.amsys.alphamanfacturas.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Registro {
    @PrimaryKey(autoGenerate = true)
    var registroId: Int = 0

    var tipoAviso: Int = 0
    var consecuenciaId: Int = 0
    var descripcion: String = ""
    var prioridadId: Int = 0

    var ubicacionTecnicaId: Int = 0
    var emplazamientoId: Int = 0
    var equipoSuperiorId: Int = 0
    var componenteId: Int = 0
    var equipoId: Int = 0
    var areaId: Int = 0
    var sistemaId: Int = 0
    var parteId: Int = 0

    var modoFallaOrigenId: Int = 0
    var metodoDeteccionOrigenId: Int = 0
    var ordenTrabajoId: Int = 0
    var fecha: String = ""
    var comentarioRegistro: String = ""

    var inicioParada: String = ""
    var finParada: String = ""
    var claseParadaId: Int = 0
    var tipoParadaId: Int = 0
    var subTipoParadaId: Int = 0

    var modoFallaId: Int = 0
    var metodoDeteccionId: Int = 0
    var mecanismoFallaId: Int = 0
    var impactoId: Int = 0
    var causaId: Int = 0
    var comentario: String = ""


    // form1
    var consecuenciaIdNombre: String = ""
    var prioridadIdNombre: String = ""



}