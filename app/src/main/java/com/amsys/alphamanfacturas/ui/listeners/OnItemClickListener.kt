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
}