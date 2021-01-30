package com.amsys.alphamanfacturas.data.local.model

open class Reporte {

    var estado: String = ""
    var cantidad: Int = 0
    var color: String = ""

    constructor()

    constructor(estado: String, cantidad: Int, color: String) {
        this.estado = estado
        this.cantidad = cantidad
        this.color = color
    }

    fun lista(): List<Reporte> {
        return listOf(
            Reporte("Emitido", 4, "#d0cece"),
            Reporte("LIBERADO", 0, "#4472c4"),
            Reporte("APROBADO", 0, "#00b050"),
            Reporte("ATENDIDO", 0, "#00803a")
        )
    }



    fun listaInspeccion(): List<Reporte> {
        return listOf(
            Reporte("ABIERTA", 0, "#d0cece"),
            Reporte("PLANEACIÓN", 0, "#4472c4"),
            Reporte("PROGRAMACIÓN", 0, "#00b050"),
            Reporte("REPROGRAMACIÓN", 0, "#00803a"),
            Reporte("EJECUCIÓN", 0, "#ffc000")
        )
    }
}