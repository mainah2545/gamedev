package com.kanyarat.gamedev

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ImageListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_list)

        val items = RemoteUrls.items
        val recycler = findViewById<RecyclerView>(R.id.imageRecycler)
        recycler.layoutManager = GridLayoutManager(this, 2)
        recycler.adapter = ImageListAdapter(items) { index ->
            startActivity(
                Intent(this, ImageDetailActivity::class.java).putExtra(EXTRA_INDEX, index)
            )
        }
        findViewById<View>(R.id.emptyText).visibility =
            if (items.isEmpty()) View.VISIBLE else View.GONE
    }
}
