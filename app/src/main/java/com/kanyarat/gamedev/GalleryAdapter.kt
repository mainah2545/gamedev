package com.kanyarat.gamedev

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView

class GalleryAdapter : RecyclerView.Adapter<GalleryAdapter.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.galleryImage)
        val progress: ProgressBar = view.findViewById(R.id.galleryProgress)
        val error: View = view.findViewById(R.id.galleryError)
        var task: LoadTask? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery_page, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        load(holder, position % RemoteUrls.GALLERY_COUNT)
    }

    private fun load(holder: Holder, index: Int) {
        holder.task?.cancel()
        holder.progress.visibility = View.VISIBLE
        holder.error.visibility = View.GONE
        holder.image.setImageDrawable(null)
        holder.itemView.setOnClickListener {
            if (holder.error.visibility == View.VISIBLE) {
                load(holder, index)
            }
        }
        holder.task = RemoteLoader.loadImage(RemoteUrls.galleryImageUrl(index), 1024) { result ->
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

    override fun getItemCount(): Int = if (RemoteUrls.GALLERY_COUNT > 0) Int.MAX_VALUE else 0
}
