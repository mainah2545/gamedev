package com.kanyarat.gamedev

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class GalleryTransformer : ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        val distance = abs(position).coerceAtMost(1f)
        val scale = 1f - distance * 0.15f
        page.alpha = 1f - distance * 0.5f
        page.scaleX = scale
        page.scaleY = scale
    }
}
