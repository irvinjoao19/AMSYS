package com.amsys.alphamanfacturas.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amsys.alphamanfacturas.R
import com.amsys.alphamanfacturas.data.local.model.PuntoMedida
import com.amsys.alphamanfacturas.helper.Util
import com.amsys.alphamanfacturas.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_form_1_inspecciones.view.*
import kotlinx.android.synthetic.main.cardview_form_1_inspecciones.view.editText1
import kotlinx.android.synthetic.main.cardview_form_1_inspecciones.view.editText2
import kotlinx.android.synthetic.main.cardview_form_1_inspecciones.view.editText3
import kotlinx.android.synthetic.main.cardview_form_1_inspecciones.view.textView1
import kotlinx.android.synthetic.main.cardview_form_1_inspecciones.view.textView11
import kotlinx.android.synthetic.main.cardview_form_1_inspecciones.view.textView2
import kotlinx.android.synthetic.main.cardview_form_1_inspecciones.view.textView3
import kotlinx.android.synthetic.main.cardview_form_1_inspecciones.view.textView4
import kotlinx.android.synthetic.main.cardview_form_1_inspecciones.view.textView5
import kotlinx.android.synthetic.main.cardview_form_1_inspecciones.view.textView7
import kotlinx.android.synthetic.main.cardview_form_1_inspecciones.view.textView9
import kotlinx.android.synthetic.main.cardview_form_inspecciones.view.*

class PuntoMedidaAdapter(private val listener: OnItemClickListener.PuntoMedidaListener) :
    RecyclerView.Adapter<PuntoMedidaAdapter.ViewHolder>() {

    private var d = emptyList<PuntoMedida>()
    private var dList: ArrayList<PuntoMedida> = ArrayList()

    fun addItems(list: List<PuntoMedida>) {
        d = list
        dList = ArrayList(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_form_1_inspecciones, parent, false)
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
        fun bind(a: PuntoMedida, listener: OnItemClickListener.PuntoMedidaListener) =
            with(itemView) {
                textView1.text = a.equipoCodigo
                textView2.text = String.format("Muestra N°%s", a.nroMuestra)
                textView3.text = a.equipoNombre
                Util.getTextStyleHtml(
                    "<font style='color:#29A2E6'>UMS:</font> ${a.unidadMedida}",
                    textView4
                )
                Util.getTextStyleHtml(
                    "<font style='color:#29A2E6'>PM:</font> ${a.puntoMedida}",
                    textView5
                )
                if (a.editable) {
                    editText1.setText(a.fechaMuestra)
                    editText2.setText(a.valor)
                    editText3.setText(a.comentario)

                    editText1.setOnClickListener { v ->
                        listener.onItemClick(
                            a, v, adapterPosition
                        )
                    }
                    editText2.setOnEditorActionListener { editText, p1, p2 ->
                        listener.onEditorAction(a, editText, p1, p2)
                    }
                    editText3.setOnEditorActionListener { editText, p1, p2 ->
                        listener.onEditorAction(a, editText, p1, p2)
                    }

                    textView7.setEndIconOnClickListener {
                        listener.onItemClick(a, textView7, adapterPosition)
                    }
                    textView9.setEndIconOnClickListener {
                        listener.onItemClick(a, textView9, adapterPosition)
                    }
                    textView11.setEndIconOnClickListener {
                        listener.onItemClick(a, textView11, adapterPosition)
                    }
                } else {
                    editText1.isEnabled = false
                    editText2.isEnabled = false
                    editText3.isEnabled = false
                }
            }
    }
}