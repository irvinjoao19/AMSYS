package com.amsys.alphamanfacturas.ui.adapters

import android.graphics.Color
import android.view.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.amsys.alphamanfacturas.R
import com.amsys.alphamanfacturas.data.local.model.Aviso
import com.amsys.alphamanfacturas.helper.Util
import com.amsys.alphamanfacturas.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_avisos.view.*

class AvisoAdapter(private val listener: OnItemClickListener.AvisoListener) :
    RecyclerView.Adapter<AvisoAdapter.ViewHolder>() {

    private var d = emptyList<Aviso>()
    private var dList: ArrayList<Aviso> = ArrayList()

    fun addItems(list: List<Aviso>) {
        d = list
        dList = ArrayList(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_avisos, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(dList[position], position, listener)
    }

    override fun getItemCount(): Int {
        return dList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(a: Aviso, position: Int, listener: OnItemClickListener.AvisoListener) =
            with(itemView) {
                if (position % 2 == 1) {
                    card.setCardBackgroundColor(
                        ContextCompat.getColor(itemView.context, R.color.colorPrimary)
                    )
                    view1.setBackgroundColor(Color.WHITE)
                    view2.setBackgroundColor(Color.WHITE)
                    view3.setBackgroundColor(Color.WHITE)

                    textView1.setTextColor(Color.WHITE)
                    textView2.setTextColor(Color.WHITE)
                    textView3.setTextColor(Color.WHITE)
                    textView4.setTextColor(Color.WHITE)
                    textView5.setTextColor(Color.WHITE)
                    textView6.setTextColor(Color.WHITE)
                    textView7.setTextColor(Color.WHITE)
                    textView8.setTextColor(Color.WHITE)
                    textView9.setTextColor(Color.WHITE)
                    textView10.setTextColor(Color.WHITE)
                    textView11.setTextColor(Color.WHITE)
                } else {
                    card.setCardBackgroundColor(
                        ContextCompat.getColor(itemView.context, R.color.colorWhite)
                    )
                }

                textView1.text = a.tipo
                textView2.text = a.codigo
                textView3.text = a.ubicacionCodigo
                textView4.text = a.ubicacionNombre
                textView5.text = a.equipoCodigo
                textView6.text = a.equipoNombre
                if (a.inicioParada.isEmpty()) {
                    textView10.visibility = View.GONE
                } else {
                    textView10.visibility = View.VISIBLE
                    Util.getTextStyleHtml(
                        String.format(
                            "<strong> Fecha Inicio Parada :</strong> %s",
                            a.inicioParada
                        ), textView10
                    )
                }
                if (a.finParada.isEmpty()) {
                    textView11.visibility = View.GONE
                } else {
                    textView11.visibility = View.VISIBLE
                    Util.getTextStyleHtml(
                        String.format(
                            "<strong> Fecha Fin Parada :</strong> %s",
                            a.finParada
                        ), textView11
                    )
                }
                textView7.text = a.comentarioRegistro
                textView8.text = a.estado
                textView9.text = a.fechaRegistro
                itemView.setOnLongClickListener {
                    listener.onItemClick(a, it, adapterPosition)
                    true
                }
            }
    }
}