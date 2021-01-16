package com.amsys.alphamanfacturas.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.Filter
import com.amsys.alphamanfacturas.R
import com.amsys.alphamanfacturas.data.local.model.Impacto
import com.amsys.alphamanfacturas.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_combo.view.*
import java.util.*
import kotlin.collections.ArrayList

class ImpactoAdapter(private val listener: OnItemClickListener.ImpactoListener) :
    RecyclerView.Adapter<ImpactoAdapter.ViewHolder>() {

    private var count = emptyList<Impacto>()
    private var countList: ArrayList<Impacto> = ArrayList()

    fun addItems(list: List<Impacto>) {
        count = list
        countList = ArrayList(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_combo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(countList[position], listener)
    }

    override fun getItemCount(): Int {
        return countList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(d: Impacto, listener: OnItemClickListener.ImpactoListener) = with(itemView) {
            textViewTitulo.text = d.nombre
            itemView.setOnClickListener { v -> listener.onItemClick(d, v, adapterPosition) }
        }
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                return FilterResults()
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                countList.clear()
                val keyword = charSequence.toString()
                if (keyword.isEmpty()) {
                    countList.addAll(count)
                } else {
                    val filteredList = ArrayList<Impacto>()
                    for (m: Impacto in count) {
                        if (m.nombre.toLowerCase(Locale.getDefault()).contains(keyword)) {
                            filteredList.add(m)
                        }
                    }
                    countList = filteredList
                }
                notifyDataSetChanged()
            }
        }
    }
}