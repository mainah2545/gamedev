package com.kanyarat.gamedev

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnImages).setOnClickListener {
            startActivity(Intent(this, ImageListActivity::class.java))
        }
        findViewById<Button>(R.id.btnTexts).setOnClickListener {
            startActivity(Intent(this, TextListActivity::class.java))
        }
        findViewById<Button>(R.id.btnSummary).setOnClickListener {
            startActivity(Intent(this, SummaryActivity::class.java))
        }
        findViewById<Button>(R.id.btnGallery).setOnClickListener {
            startActivity(Intent(this, GalleryActivity::class.java))
        }
    }
}
