package com.amsys.alphamanfacturas.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amsys.alphamanfacturas.R
import com.amsys.alphamanfacturas.data.local.model.Equipo
import com.amsys.alphamanfacturas.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_equipo.view.*

class EquipoAdapter(private val listener: OnItemClickListener.EquipoListener) :
    RecyclerView.Adapter<EquipoAdapter.ViewHolder>() {

    private var d = emptyList<Equipo>()

    fun addItems(list: List<Equipo>) {
        d = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_equipo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(d[position], listener)
    }

    override fun getItemCount(): Int {
        return d.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(e: Equipo, listener: OnItemClickListener.EquipoListener) = with(itemView) {
            textView1.text = e.codigo
            textView2.text = e.nombre
            itemView.setOnClickListener { v -> listener.onItemClick(e, v, adapterPosition) }
        }
    }
}