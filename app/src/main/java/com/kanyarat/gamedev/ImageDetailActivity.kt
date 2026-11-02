package com.kanyarat.gamedev

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

class ImageDetailActivity : BaseActivity() {

    private var task: LoadTask? = null
    private lateinit var item: GameItem
    private lateinit var image: ImageView
    private lateinit var progress: ProgressBar
    private lateinit var errorBox: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val index = intent.getIntExtra(EXTRA_INDEX, -1)
        if (index !in RemoteUrls.items.indices) {
            Toast.makeText(this, R.string.invalid_index, Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        setContentView(R.layout.activity_image_detail)
        item = RemoteUrls.items[index]
        image = findViewById(R.id.detailImage)
        progress = findViewById(R.id.detailProgress)
        errorBox = findViewById(R.id.errorBox)
        findViewById<TextView>(R.id.detailName).text = item.name
        findViewById<Button>(R.id.retryButton).setOnClickListener { load() }
        load()
    }

    private fun load() {
        task?.cancel()
        progress.visibility = View.VISIBLE
        errorBox.visibility = View.GONE
        image.setImageDrawable(null)
        task = RemoteLoader.loadImage(RemoteUrls.imageUrl(item), 1024) { result ->
            progress.visibility = View.GONE
            result.onSuccess { image.setImageBitmap(it) }
                .onFailure { errorBox.visibility = View.VISIBLE }
        }
    }

    override fun onDestroy() {
        task?.cancel()
        super.onDestroy()
    }
}
