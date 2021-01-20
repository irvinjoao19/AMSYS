package com.amsys.alphamanfacturas.ui.listeners

import android.view.View
import com.amsys.alphamanfacturas.data.local.model.*

interface OnItemClickListener {
    interface AvisoListener {
        fun onItemClick(a: Aviso, v: View, position: Int)
    }

    interface DeteccionListener {
        fun onItemClick(d: Deteccion, v: View, position: Int)
    }

    interface MecanismoFallaListener {
        fun onItemClick(m: MecanismoFalla, v: View, position: Int)
    }

    interface ImpactoListener {
        fun onItemClick(m: Impacto, v: View, position: Int)
    }

    interface CausaFallaListener {
        fun onItemClick(c: CausaFalla, v: View, position: Int)
    }

    interface ConsecuenciaListener {
        fun onItemClick(c: Consecuencia, v: View, position: Int)
    }

    interface PrioridadListener {
        fun onItemClick(p: Prioridad, v: View, position: Int)
    }

    interface ComboListener {
        fun onItemClick(q: Query, v: View, position: Int)
    }

    interface EquipoListener {
        fun onItemClick(e: Equipo, v: View, position: Int)
    }

    interface ModoFallaListener {
        fun onItemClick(m: ModoFalla, v: View, position: Int)
    }

    interface ParadaListener {
        fun onItemClick(p: Parada, v: View, position: Int)
    }

    interface TipoParadaListener {
        fun onItemClick(p: TipoParada, v: View, position: Int)
    }

    interface SubTipoParadaListener {
        fun onItemClick(p: SubTipoParada, v: View, position: Int)
    }
}