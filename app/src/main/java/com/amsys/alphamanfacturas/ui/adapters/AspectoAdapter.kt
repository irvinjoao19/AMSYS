package com.amsys.alphamanfacturas.ui.adapters

import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amsys.alphamanfacturas.R
import com.amsys.alphamanfacturas.data.local.model.Aspecto
import com.amsys.alphamanfacturas.ui.listeners.OnItemClickListener
import com.google.android.material.textfield.TextInputLayout
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

                when (a.tipoAspecto) {
                    1 -> {
                        if (a.tipoValor == 2) {
                            editText2.inputType =
                                InputType.TYPE_CLASS_NUMBER
                        }
                    }
                    2 -> {
                        editText2.isFocusable = false
                        editText2.setOnClickListener { v ->
                            listener.onItemClick(
                                a, v, adapterPosition
                            )
                        }
                    }
                }

                editText3.setText(a.comentario)

                if (a.editable) {
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

                    textView7.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                    textView7.isEndIconVisible = true
                    textView9.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                    textView9.isEndIconVisible = true
                    textView11.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                    textView11.isEndIconVisible = true

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
                    view.visibility = View.VISIBLE
                    editText1.isEnabled = false
                    editText2.isEnabled = false
                    editText3.isEnabled = false
                    textView7.isEndIconVisible = false
                    textView9.isEndIconVisible = false
                    textView11.isEndIconVisible = false
                }
            }
    }
}