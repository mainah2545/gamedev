package com.kanyarat.gamedev

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView

class SummaryActivity : BaseActivity() {

    private val tasks = mutableListOf<LoadTask>()
    private var pending = 0
    private var imageOk = 0
    private var textOk = 0

    private lateinit var progress: ProgressBar
    private lateinit var status: TextView
    private lateinit var imagesLine: TextView
    private lateinit var textsLine: TextView
    private lateinit var retry: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)
        progress = findViewById(R.id.summaryProgress)
        status = findViewById(R.id.summaryStatus)
        imagesLine = findViewById(R.id.summaryImages)
        textsLine = findViewById(R.id.summaryTexts)
        retry = findViewById(R.id.summaryRetry)
        retry.setOnClickListener { start() }
        start()
    }

    private fun start() {
        tasks.forEach { it.cancel() }
        tasks.clear()
        val items = RemoteUrls.items
        imageOk = 0
        textOk = 0
        imagesLine.text = ""
        textsLine.text = ""
        if (items.isEmpty()) {
            progress.visibility = View.GONE
            status.setText(R.string.empty_list)
            retry.isEnabled = true
            return
        }
        pending = items.size * 2
        progress.visibility = View.VISIBLE
        status.setText(R.string.loading)
        retry.isEnabled = false
        items.forEach { item ->
            tasks += RemoteLoader.loadImage(RemoteUrls.imageUrl(item), 32) { result ->
                if (result.isSuccess) imageOk++
                finishOne()
            }
            tasks += RemoteLoader.loadText(RemoteUrls.textUrl(item)) { result ->
                if (result.isSuccess) textOk++
                finishOne()
            }
        }
    }

    private fun finishOne() {
        pending--
        if (pending > 0) return
        val total = RemoteUrls.items.size
        progress.visibility = View.GONE
        retry.isEnabled = true
        imagesLine.text = getString(R.string.summary_images, imageOk, total, total - imageOk)
        textsLine.text = getString(R.string.summary_texts, textOk, total, total - textOk)
        status.setText(
            when {
                imageOk + textOk == 0 -> R.string.summary_all_failed
                imageOk == total && textOk == total -> R.string.summary_ok
                else -> R.string.summary_partial
            }
        )
    }

    override fun onDestroy() {
        tasks.forEach { it.cancel() }
        super.onDestroy()
    }
}
