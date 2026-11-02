package com.kanyarat.gamedev

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TextListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_list)

        val items = RemoteUrls.items
        val recycler = findViewById<RecyclerView>(R.id.textRecycler)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = TextListAdapter(items) { index ->
            startActivity(
                Intent(this, TextDetailActivity::class.java).putExtra(EXTRA_INDEX, index)
            )
        }
        findViewById<View>(R.id.emptyText).visibility =
            if (items.isEmpty()) View.VISIBLE else View.GONE
    }
}
