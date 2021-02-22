package com.amsys.alphamanfacturas.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amsys.alphamanfacturas.R
import com.amsys.alphamanfacturas.data.local.model.Partes
import com.amsys.alphamanfacturas.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_combo.view.*

class PartesAdapter(private val listener: OnItemClickListener.PartesListener) :
    RecyclerView.Adapter<PartesAdapter.ViewHolder>() {

    private var menu = emptyList<Partes>()

    fun addItems(list: List<Partes>) {
        menu = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_combo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(menu[position], listener)
    }

    override fun getItemCount(): Int {
        return menu.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(q: Partes, listener: OnItemClickListener.PartesListener) = with(itemView) {
            textViewTitulo.text = q.nombre
            itemView.setOnClickListener { v -> listener.onItemClick(q, v, adapterPosition) }
        }
    }
}