package com.kanyarat.gamedev

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TextListAdapter(
    private val items: List<GameItem>,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<TextListAdapter.Holder>() {

    class Holder(val label: TextView) : RecyclerView.ViewHolder(label)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_text, parent, false)
        return Holder(view as TextView)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.label.text = items[position].name
        holder.label.setOnClickListener {
            val adapterPosition = holder.layoutPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onClick(adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
