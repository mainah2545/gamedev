package com.kanyarat.gamedev

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ImageListAdapter(
    private val items: List<GameItem>,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<ImageListAdapter.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.itemImage)
        val progress: ProgressBar = view.findViewById(R.id.itemProgress)
        val error: TextView = view.findViewById(R.id.itemError)
        val name: TextView = view.findViewById(R.id.itemName)
        var task: LoadTask? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]
        holder.task?.cancel()
        holder.name.text = item.name
        holder.image.setImageDrawable(null)
        holder.error.visibility = View.GONE
        holder.progress.visibility = View.VISIBLE
        holder.itemView.setOnClickListener {
            val adapterPosition = holder.layoutPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onClick(adapterPosition)
            }
        }
        holder.task = RemoteLoader.loadImage(RemoteUrls.imageUrl(item), 400) { result ->
            holder.progress.visibility = View.GONE
            result.onSuccess { holder.image.setImageBitmap(it) }
                .onFailure { holder.error.visibility = View.VISIBLE }
        }
    }

    override fun onViewRecycled(holder: Holder) {
        holder.task?.cancel()
        holder.task = null
        super.onViewRecycled(holder)
    }

    override fun getItemCount(): Int = items.size
}
