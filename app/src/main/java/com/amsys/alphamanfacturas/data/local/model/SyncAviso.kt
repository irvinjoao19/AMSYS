package com.amsys.alphamanfacturas.data.local.model

open class SyncAviso {
    var consecuencias: List<Consecuencia> = ArrayList()
    var prioridades: List<Prioridad> = ArrayList()
    var clasesParada: List<Parada> = ArrayList()
    var metodosDeteccion: List<Deteccion> = ArrayList()
    var mecanismosFalla: List<MecanismoFalla> = ArrayList()
    var impactos: List<Impacto> = ArrayList()
    var causasFalla: List<CausaFalla> = ArrayList()
}