package com.amsys.alphamanfacturas.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amsys.alphamanfacturas.R
import com.amsys.alphamanfacturas.data.local.model.InspeccionFile
import com.amsys.alphamanfacturas.helper.Util
import com.amsys.alphamanfacturas.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_file.view.*

class InspeccionFileAdapter(private val listener: OnItemClickListener.InspeccionFileListener) :
    RecyclerView.Adapter<InspeccionFileAdapter.ViewHolder>() {

    private var d = emptyList<InspeccionFile>()

    fun addItems(list: List<InspeccionFile>) {
        d = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_file, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(d[position], listener)
    }

    override fun getItemCount(): Int {
        return d.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(a: InspeccionFile, listener: OnItemClickListener.InspeccionFileListener) =
            with(itemView) {
                textView1.text = a.name
                textView2.text = a.type
                textView3.text = Util.getStringSizeLengthFile(a.size)
                img2.setOnClickListener { v -> listener.onItemClick(a, v, adapterPosition) }
            }
    }
}