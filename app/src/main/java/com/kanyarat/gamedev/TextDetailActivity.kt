package com.kanyarat.gamedev

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

class TextDetailActivity : BaseActivity() {

    private var task: LoadTask? = null
    private lateinit var item: GameItem
    private lateinit var body: TextView
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
        setContentView(R.layout.activity_text_detail)
        item = RemoteUrls.items[index]
        body = findViewById(R.id.textBody)
        progress = findViewById(R.id.textProgress)
        errorBox = findViewById(R.id.errorBox)
        findViewById<TextView>(R.id.textTitle).text = item.name
        findViewById<Button>(R.id.retryButton).setOnClickListener { load() }
        load()
    }

    private fun load() {
        task?.cancel()
        progress.visibility = View.VISIBLE
        errorBox.visibility = View.GONE
        body.text = ""
        task = RemoteLoader.loadText(RemoteUrls.textUrl(item)) { result ->
            progress.visibility = View.GONE
            result.onSuccess { body.text = it }
                .onFailure { errorBox.visibility = View.VISIBLE }
        }
    }

    override fun onDestroy() {
        task?.cancel()
        super.onDestroy()
    }
}
