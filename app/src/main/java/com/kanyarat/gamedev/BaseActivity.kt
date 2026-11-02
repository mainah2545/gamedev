package com.kanyarat.gamedev

import android.os.Build
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

abstract class BaseActivity : AppCompatActivity() {

    override fun setContentView(@LayoutRes layoutResID: Int) {
        super.setContentView(layoutResID)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (Build.VERSION.SDK_INT >= 35) {
            applyEdgeToEdgePadding()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun applyEdgeToEdgePadding() {
        val content = findViewById<View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(content) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
        ViewCompat.requestApplyInsets(content)
    }
}
