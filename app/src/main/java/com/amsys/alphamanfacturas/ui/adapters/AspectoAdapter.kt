package com.amsys.alphamanfacturas.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amsys.alphamanfacturas.R
import com.amsys.alphamanfacturas.data.local.model.Aspecto
import com.amsys.alphamanfacturas.helper.Util
import com.amsys.alphamanfacturas.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_form_inspecciones.view.*

class AspectoAdapter(private val listener: OnItemClickListener.AspectoListener) :
    RecyclerView.Adapter<AspectoAdapter.ViewHolder>() {

    private var d = emptyList<Aspecto>()
    private var dList: ArrayList<Aspecto> = ArrayList()

    fun addItems(list: List<Aspecto>) {
        d = list
        dList = ArrayList(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_form_inspecciones, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(dList[position], listener)
    }

    override fun getItemCount(): Int {
        return dList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(a: Aspecto, listener: OnItemClickListener.AspectoListener) =
            with(itemView) {
                textView1.text = a.equipoCodigo
                textView2.text = String.format("Muestra N° %s", a.nroMuestra)
                textView3.text = a.equipoNombre
                textView4.text = a.aspecto
                textView5.visibility = View.GONE
                editText1.setText(a.fechaMuestra)
                editText2.setText(a.valor)
                editText3.setText(a.comentario)

                editText1.setOnClickListener { v -> listener.onItemClick(a, v, adapterPosition) }
                editText2.setOnEditorActionListener { editText, p1, p2 ->
                    listener.onEditorAction(a, editText, p1, p2)
                }
                editText3.setOnEditorActionListener { editText, p1, p2 ->
                    listener.onEditorAction(a, editText, p1, p2)
                }
            }
    }

}